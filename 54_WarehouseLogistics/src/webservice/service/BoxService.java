package webservice.service;

import java.util.ArrayList;
import java.util.List;

import model.AccountModel;
import model.BoxModel;
import model.ItemAmountModel;
import model.ItemBoxModel;
import model.ItemModel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exceptions.NullAccountException;
import exceptions.NullBoxException;
import service.database.AccountDatabase;
import service.database.BoxDatabase;
import webservice.requestmodel.AddItemModel;
import webservice.requestmodel.BoxGetModel;
import webservice.responsemodel.BoxWSModel;

@RestController()
@RequestMapping("/webservice/box")
public class BoxService
{
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<BoxWSModel> getMyBoxList(@RequestBody BoxGetModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase();
		AccountModel account;
		BoxDatabase boxDatabase = new BoxDatabase();
		account = accountDatabae.findByToken(model.getToken());
		List<BoxModel> list = boxDatabase.listByOwner(account.getId());
		List<BoxWSModel> boxList = new ArrayList<BoxWSModel>();
		for (BoxModel b : list)
		{
			if (b.getHashCode() != null)
				continue;
			BoxWSModel box = new BoxWSModel();
			box.setId(b.getId());
			box.setName(b.getName());
			box.setLocation(b.getLocation());
			boxList.add(box);
		}
		return boxList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}", produces = "application/json")
	public @ResponseBody List<ItemAmountModel> getMyBoxItem(@PathVariable("id") int id, @RequestBody BoxGetModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase();
		AccountModel account;
		BoxDatabase boxDatabase = new BoxDatabase();
		BoxModel box = new BoxModel();
		account = accountDatabae.findByToken(model.getToken());
		box = boxDatabase.find(id);
		if (account.getId() != box.getOwner())
			throw new Exception("Not allow to use the box.");
		List<ItemAmountModel> list = boxDatabase.listItemAmount(box);
		return list;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void addItemToBox(@PathVariable("id") int id, @RequestBody AddItemModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase();
		AccountModel account;
		BoxDatabase boxDatabase = new BoxDatabase();
		BoxModel box = new BoxModel();
		ItemModel item;
		ItemBoxModel itemBox;
		account = accountDatabae.findByToken(model.getToken());
		box = boxDatabase.find(id);
		if (account.getId() != box.getOwner())
			throw new Exception("Not allow to use the box.");
		item = boxDatabase.findItemByName(model.getItemName());
		if (item == null)
		{
			item = new ItemModel();
			item.setName(model.getItemName());
			boxDatabase.createItem(item);
			item = boxDatabase.findItemByName(model.getItemName());
			itemBox = new ItemBoxModel();
			itemBox.setItemId(item.getId());
			itemBox.setBoxId(id);
			itemBox.setAmount(model.getAmount());
			boxDatabase.createItemBox(itemBox);
		}
		else
		{
			itemBox = boxDatabase.findItemBox(id, item.getId());
			if (itemBox == null)
			{
				itemBox = new ItemBoxModel();
				itemBox.setItemId(item.getId());
				itemBox.setBoxId(id);
				itemBox.setAmount(model.getAmount());
				boxDatabase.createItemBox(itemBox);
			}
			else
			{
				int amount = itemBox.getAmount();
				amount += model.getAmount();
				itemBox.setAmount(amount);
				boxDatabase.updateItemBox(itemBox);
			}
		}
	}
	
	
	@ExceptionHandler(NullAccountException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullAccountException(NullAccountException e) {}
	
	@ExceptionHandler(NullBoxException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullBoxExceptionException(NullBoxException e) {}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception e) { e.printStackTrace(); }
}
