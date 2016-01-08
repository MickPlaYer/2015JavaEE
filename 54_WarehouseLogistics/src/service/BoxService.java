package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import converter.MD5Converter;
import service.database.BoxDatabase;
import viewmodel.BoxRenewer;
import viewmodel.ModifyItemModel;
import viewmodel.BoxToBoxModel;
import viewmodel.BoxToLocationModel;
import viewmodel.BoxSalesman;
import viewmodel.ItemToBoxModel;
import webservice.requestmodel.PayModel;
import exceptions.BoxActivateFailException;
import exceptions.BoxDuplicateActivateException;
import exceptions.BoxPeriodException;
import exceptions.BoxPrivilegeException;
import exceptions.BoxUnactivatedException;
import exceptions.IllegalPeriodException;
import exceptions.ItemNotEnoughException;
import exceptions.NullBoxException;
import exceptions.NullItemBoxException;
import exceptions.NullItemException;
import model.BoxModel;
import model.ItemAmountModel;
import model.ItemBoxModel;
import model.ItemModel;

public class BoxService
{
	private BoxDatabase boxDatabase;
	private int accountId;

	public BoxService(int accountId, SessionFactory sessionFactory) throws Exception
	{
		boxDatabase = new BoxDatabase(sessionFactory);
		this.accountId = accountId;
	}
	
	public List<BoxModel> getBoxList() throws Exception
	{
		return boxDatabase.listByOwner(accountId);
	}

	public List<BoxModel> getUseableBoxList() throws Exception
	{
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		List<BoxModel> boxlist = new ArrayList<BoxModel>();
		try
		{
			List<BoxModel> list = boxDatabase.listByOwner(accountId);
			for(BoxModel box : list)
				if (box.getHashCode() == null && today.before(box.getDeadline()))
					boxlist.add(box);
		}
		catch (NullBoxException exception) { }
		return boxlist;
	}
	
	public BoxSalesman buyNewBox(BoxSalesman salesman) throws Exception
	{
		BoxModel box = new BoxModel();
		PayModel pay = new PayModel();
		String hashCode = "NiSeMoNo" + salesman.getBoxName() + new Date().toString();
		hashCode = MD5Converter.convert(hashCode);
		hashCode = "bnb" + hashCode.substring(3);
		Date date = setupPeriodTypeAndPrice(new Date(), salesman.getBoxPeriod(), pay);
		box.setOwner(accountId);
		box.setName(salesman.getBoxName());
		box.setLocation(salesman.getBoxLocation());
		box.setDeadline(date);
		box.setHashCode(hashCode);
		boxDatabase.create(box);
		salesman.setBox(box);
		salesman.setPay(pay);
		return salesman;
	}
	
	public BoxRenewer renewBox(BoxRenewer renewer) throws Exception
	{
		BoxModel box = getBoxWithoutDateCheck(renewer.getBoxId());
		PayModel pay = new PayModel();
		String hashCode = "NekoMoNo" + box.getName() + new Date().toString();
		hashCode = MD5Converter.convert(hashCode);
		Date date;
		if (box.getDeadline().after(new Date()))
			date = box.getDeadline();
		else
			date = new Date();
		date = setupPeriodTypeAndPrice(date, renewer.getBoxPeriod(), pay);
		hashCode = "rnb" + (pay.getValue() / 100) + hashCode.substring(4);
		System.out.println("hashCode: " + hashCode);
		box.setDeadline(date);
		box.setHashCode(hashCode);
		boxDatabase.update(renewer.getBoxId(), box);
		renewer.setBox(box);
		renewer.setPay(pay);
		return renewer;
	}

	public BoxRenewer activateBox(BoxRenewer renewer) throws Exception
	{
		BoxModel box = getBoxWithoutDateCheck(renewer.getBoxId());
		PayModel pay = new PayModel();
		String key = box.getHashCode();
		if (key == null)
			key = "rnb0";
		System.out.println("key.substring(0, 4): " + key.substring(0, 4));
		if ("rnb".equals(key.substring(0, 3)))
		{
			System.out.println("key.substring(3, 4): " + key.substring(3, 4));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(box.getDeadline());
			switch (key.substring(3, 4))
			{
				case "1":
					calendar.add(Calendar.MONTH, -1);
					break;
				case "3":
					calendar.add(Calendar.MONTH, -6);
					break;
				case "5":
					calendar.add(Calendar.YEAR, -1);
					break;
			}
			box.setDeadline(calendar.getTime());
			boxDatabase.update(box.getId(), box);
			return renewBox(renewer);
		}
		System.out.println("box.getHashCode(): " + box.getHashCode());
		String hashCode = "NiSeMoNo" + box.getName() + new Date().toString();
		hashCode = MD5Converter.convert(hashCode);
		hashCode = "bnb" + hashCode.substring(3);
		Date date = setupPeriodTypeAndPrice(new Date(), renewer.getBoxPeriod(), pay);
		box.setDeadline(date);
		box.setHashCode(hashCode);
		boxDatabase.update(renewer.getBoxId(), box);
		renewer.setBox(box);
		renewer.setPay(pay);
		return renewer;
	}
	
	private Date setupPeriodTypeAndPrice(Date date, String period, PayModel pay) throws IllegalPeriodException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (period)
		{
			case "year":
				calendar.add(Calendar.YEAR, 1);
				pay.setValue(500);
				break;
			case "helf-year":
				calendar.add(Calendar.MONTH, 6);
				pay.setValue(350);
				break;
			case "month":
				calendar.add(Calendar.MONTH, 1);
				pay.setValue(100);
				break;
			default:
				throw new IllegalPeriodException();
		}
		return calendar.getTime();
	}
	
	public void addItemToBox(int boxId, ModifyItemModel model) throws Exception
	{
		BoxModel box = getBox(boxId);
		ItemModel item;
		ItemBoxModel itemBox;
		try
		{
			item = boxDatabase.findItemByName(model.getName());
			try
			{
				itemBox = boxDatabase.findItemBox(box.getId(), item.getId());
				// When a exist item add into box that has some same items;
				itemBox.addAmount(model.getAmount());
				boxDatabase.updateItemBox(itemBox);
			}
			catch (NullItemBoxException exception)
			{
				// When a exist item add into box that has no any same items;
				itemBox = new ItemBoxModel(item.getId(), box.getId(), model.getAmount());
				boxDatabase.createItemBox(itemBox);
			}
		}
		catch (NullItemException exception)
		{
			// When a new item add into box.
			item = new ItemModel();
			item.setName(model.getName());
			item = boxDatabase.createItem(item);
			itemBox = new ItemBoxModel(item.getId(), box.getId(), model.getAmount());
			boxDatabase.createItemBox(itemBox);
		}
	}

	public void removeItemFromBox(int boxId, ModifyItemModel model) throws Exception
	{
		BoxModel box = getBox(boxId);
		ItemModel item = boxDatabase.findItemByName(model.getName());
		ItemBoxModel itemBox = boxDatabase.findItemBox(box.getId(), item.getId());
		itemBox.addAmount(-model.getAmount());
		if (itemBox.getAmount() == 0)
		{
			boxDatabase.deleteItemBox(itemBox);
			try	{ boxDatabase.deleteItem(item);	}
			catch (ConstraintViolationException exception) { boxDatabase.disconnect(); }
		}
		else if (itemBox.getAmount() > 0)
			boxDatabase.updateItemBox(itemBox);
		else if (itemBox.getAmount() < 0)
			throw new ItemNotEnoughException();
	}
	
	public BoxModel getBoxWithoutDateCheck(int boxId) throws Exception
	{
		BoxModel box = boxDatabase.find(boxId);
		if (accountId != box.getOwner())
			throw new BoxPrivilegeException();
		return box;
	}
	
	public BoxModel getBox(int boxId) throws Exception
	{
		BoxModel box = boxDatabase.find(boxId);
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		if (today.after(box.getDeadline()))
			throw new BoxPeriodException();
		if (accountId != box.getOwner())
			throw new BoxPrivilegeException();
		if (box.getHashCode() != null)
			throw new BoxUnactivatedException();
		return box;
	}

	public BoxModel findBox(int boxId) throws Exception
	{
		return boxDatabase.find(boxId);
	}
	
	public List<ItemAmountModel> getItemAmountList(int boxId) throws Exception
	{
		BoxModel box = getBox(boxId);
		return boxDatabase.listItemAmount(box);
	}

	public List<ItemAmountModel> getItemAmountList(BoxModel box) throws Exception
	{
		return boxDatabase.listItemAmount(box);
	}

	public void activateBox(int boxId, String hashCode) throws Exception
	{
		BoxModel box = boxDatabase.find(boxId);
		if (box.getHashCode() == null)
			throw new BoxDuplicateActivateException();
		if (!box.getHashCode().equals(hashCode))
			throw new BoxActivateFailException();
		box.setHashCode(null);
		boxDatabase.update(boxId, box);
	}

	public List<ItemModel> getItemList()
	{
		return boxDatabase.listItem();
	}
	
	public ItemModel findItem(int itemId) throws NullItemException
	{
		return boxDatabase.findItem(itemId);
	}

	public ItemBoxModel findItemBox(int boxId, int itemId) throws Exception
	{
		return boxDatabase.findItemBox(boxId, itemId);
	}

	public ItemModel findItemByName(String name) throws Exception
	{
		return boxDatabase.findItemByName(name);
	}

	public void deliverBoxToBox(BoxToBoxModel model, ItemModel item) throws Exception
	{
		ItemBoxModel itemFromBox = boxDatabase.findItemBox(model.getFromBoxId(), item.getId());
		ItemBoxModel itemToBox;
		try
		{ 
			itemToBox = boxDatabase.findItemBox(model.getToBoxId(), item.getId());
			boxDatabase.updateItemBox(itemToBox);
		}
		catch (NullItemBoxException exception)
		{ 
			itemToBox = new ItemBoxModel(item.getId(), model.getToBoxId(), 0);
			boxDatabase.createItemBox(itemToBox);
		}
		itemFromBox.addAmount(-model.getAmount());
		itemToBox.addAmount(model.getAmount());
		if (itemFromBox.getAmount() == 0)
			boxDatabase.deleteItemBox(itemFromBox);
		else if (itemFromBox.getAmount() > 0)
			boxDatabase.updateItemBox(itemFromBox);
		else
			throw new ItemNotEnoughException();
			boxDatabase.updateItemBox(itemToBox);
	}

	public void deliverItemToBox(int boxId, ItemToBoxModel model) throws Exception
	{
		ModifyItemModel addItemModel = new ModifyItemModel();
		addItemModel.setAmount(model.getAmount());
		addItemModel.setName(model.getItem());
		addItemToBox(boxId, addItemModel);
	}

	public void deliverBoxToLocation(int boxId, BoxToLocationModel model) throws Exception
	{
		ModifyItemModel addItemModel = new ModifyItemModel();
		addItemModel.setAmount(model.getAmount());
		addItemModel.setName(model.getItem());
		removeItemFromBox(boxId, addItemModel);
	}
}
