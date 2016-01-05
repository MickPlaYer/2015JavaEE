package spring.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullBoxException;
import model.AccountModel;
import model.BoxModel;
import model.ItemBoxModel;
import model.ItemModel;
import model.WaybillModel;
import service.database.BoxDatabase;
import service.database.WaybillDatabase;
import viewmodel.BoxToBoxModel;
import viewmodel.BoxToLocationModel;
import viewmodel.ItemToBoxModel;
import viewmodel.ItemToLocationModel;

@Controller()
@RequestMapping("/logistics")
public class LogisticsController extends SpringController
{
	@Autowired
	private HttpSession httpSession;
	
	public LogisticsController()
	{
		super("logisticsErrorPage");
	}

	@RequestMapping(value = "/{method}", method = RequestMethod.GET)
	public ModelAndView logistics(@PathVariable("method") String method) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page;
			try { page = (String)context.getBean(method + "Page"); }
			catch (Exception exception){ return getErrorModelAndView(new Exception("Page Not Found"), DB_ERROR); }
			ModelAndView view = new ModelAndView(page);
			if (method.equals("itemToLocation"))
				return view;
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			List<BoxModel> boxList = new ArrayList<BoxModel>();
			List<ItemModel> itemList;
			try
			{ 
				itemList = boxDatabase.listItem();
				List<BoxModel> list = boxDatabase.listByOwner(account.getId());
				for(BoxModel box : list)
					if (box.getHashCode() == null)
						boxList.add(box);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}

			view.addObject("BoxList", boxList);
			view.addObject("ItemList", itemList);
			return view;
		}
	}
	
	@RequestMapping(value = "/boxToBox/{bid}/{iid}", method = RequestMethod.GET)
	public ModelAndView boxToBoxWithId(@PathVariable("bid") int boxId, @PathVariable("iid") int itemId) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{	
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			BoxModel box;
			ItemModel item;
			ItemBoxModel itemBox;
			List<BoxModel> boxList = new ArrayList<BoxModel>();
			try
			{
				box = boxDatabase.find(boxId);
				if (account.getId() != box.getOwner())
					throw new Exception("No allow to use the box.");
				item = boxDatabase.findItem(itemId);
				if (item == null)
					throw new Exception("Item not fount.");
				itemBox = boxDatabase.findItemBox(box.getId(), item.getId());
				if (itemBox == null)
					throw new Exception("Item not fount in the box.");
				List<BoxModel> list = boxDatabase.listByOwner(account.getId());
				for(BoxModel b : list)
					if (b.getHashCode() == null && b.getId() != box.getId())
						boxList.add(b);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("boxToBoxWithIdPage");
			ModelAndView view = new ModelAndView(page);
			view.addObject("Box", box);
			view.addObject("Item", item);
			view.addObject("ItemBox", itemBox);
			view.addObject("BoxList", boxList);
			return view;
		}
	}
	
	@RequestMapping(value = "/boxToLocation/{bid}/{iid}", method = RequestMethod.GET)
	public ModelAndView boxToLocationWithId(@PathVariable("bid") int boxId, @PathVariable("iid") int itemId) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			BoxModel box;
			ItemModel item;
			ItemBoxModel itemBox;
			try
			{
				box = boxDatabase.find(boxId);
				if (account.getId() != box.getOwner())
					throw new Exception("No allow to use the box.");
				item = boxDatabase.findItem(itemId);
				if (item == null)
					throw new Exception("Item not fount.");
				itemBox = boxDatabase.findItemBox(box.getId(), item.getId());
				if (itemBox == null)
					throw new Exception("Item not fount in the box.");
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("boxToLocationWithIdPage");
			ModelAndView view = new ModelAndView(page);
			view.addObject("Box", box);
			view.addObject("Item", item);
			view.addObject("ItemBox", itemBox);
			return view;
		}
	}
	
	@RequestMapping(value = "/boxToBox", method = RequestMethod.POST)
	public ModelAndView deliverBoxToBox(@Valid BoxToBoxModel model, BindingResult bindingResult) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			BoxModel boxFrom, boxTo;
			ItemModel item;
			WaybillDatabase waybillDatabase = new WaybillDatabase();
			WaybillModel waybill = new WaybillModel();
			try
			{
				if (model.getFromBoxId() == model.getToBoxId())
					throw new Exception("No allow to send item to the same box.");
				boxFrom = boxDatabase.find(model.getFromBoxId());
				boxTo= boxDatabase.find(model.getToBoxId());
				if (account.getId() != boxFrom.getOwner() || account.getId() != boxTo.getOwner())
					throw new Exception("No allow to use the box.");
				item = boxDatabase.findItemByName(model.getItem());
				if (item == null)
					throw new Exception("Item not fount.");
				ItemBoxModel itemFromBox = boxDatabase.findItemBox(model.getFromBoxId(), item.getId());
				if (itemFromBox == null)
					throw new Exception("Item not exist in the box.");
				if (model.getAmount() > itemFromBox.getAmount())
					throw new Exception("Item not enough in the box.");
				ItemBoxModel itemToBox = boxDatabase.findItemBox(model.getToBoxId(), item.getId());
				if (itemToBox == null)
				{
					itemToBox = new ItemBoxModel();
					itemToBox.setId(-1);
					itemToBox.setAmount(0);
					itemToBox.setBoxId(model.getToBoxId());
					itemToBox.setItemId(item.getId());
				}
				itemFromBox.addAmount(-model.getAmount());
				itemToBox.addAmount(model.getAmount());
				waybill.setAccountId(account.getId());
				waybill.setContents(item.getName() + " x " + model.getAmount());
				waybill.setFee(model.getAmount() * 10);
				waybill.setFrom(boxFrom.getLocation());
				waybill.setTo(boxTo.getLocation());
				waybill.setStatus(2);
				if (itemFromBox.getAmount() == 0)
					boxDatabase.deleteItemBox(itemFromBox);
				else if (itemFromBox.getAmount() > 0)
					boxDatabase.updateItemBox(itemFromBox);
				else
					throw new Exception("LogisticsController Error");
				if (itemToBox.getId() == -1)
					boxDatabase.createItemBox(itemToBox);
				else
					boxDatabase.updateItemBox(itemToBox);
				waybillDatabase.create(waybill);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("delievrSuccessPage");
			return new ModelAndView("redirect:/" + page);
		}
	}
	
	@RequestMapping(value = "/boxToLocation", method = RequestMethod.POST)
	public ModelAndView deliverBoxToLocation(@Valid BoxToLocationModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		Object result = sessionCheck(httpSession);
		if (result.getClass().equals(ModelAndView.class))
			return (ModelAndView) result;
		AccountModel account = (AccountModel)result;
		BoxDatabase boxDatabase = new BoxDatabase();
		BoxModel boxFrom;
		ItemModel item;
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		try
		{
			boxFrom = boxDatabase.find(model.getFromBoxId());
			if (account.getId() != boxFrom.getOwner())
				throw new Exception("No allow to use the box.");
			item = boxDatabase.findItemByName(model.getItem());
			if (item == null)
				throw new Exception("Item not fount.");
			ItemBoxModel itemFromBox = boxDatabase.findItemBox(model.getFromBoxId(), item.getId());
			if (itemFromBox == null)
				throw new Exception("Item not exist in the box.");
			if (model.getAmount() > itemFromBox.getAmount())
				throw new Exception("Item not enough in the box.");
			itemFromBox.addAmount(-model.getAmount());
			waybill.setAccountId(account.getId());
			waybill.setContents(item.getName() + " x " + model.getAmount());
			waybill.setFee(model.getAmount() * 10);
			waybill.setFrom(boxFrom.getLocation());
			waybill.setTo(model.getLocation());
			waybill.setStatus(2);
			if (itemFromBox.getAmount() == 0)
				boxDatabase.deleteItemBox(itemFromBox);
			else if (itemFromBox.getAmount() > 0)
				boxDatabase.updateItemBox(itemFromBox);
			else
				throw new Exception("LogisticsController Error");
			waybillDatabase.create(waybill);
		}
		catch (NullBoxException exception)
		{ return getErrorModelAndView(exception, exception.getMessage()); }
		catch (Exception exception)
		{ return getErrorModelAndView(exception, DB_ERROR);	}
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("delievrSuccessPage");
			return new ModelAndView("redirect:/" + page);
		}
	}
	
	@RequestMapping(value = "/itemToBox", method = RequestMethod.POST)
	public ModelAndView deliverItemToBox(@Valid ItemToBoxModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		Object result = sessionCheck(httpSession);
		if (result.getClass().equals(ModelAndView.class))
			return (ModelAndView) result;
		AccountModel account = (AccountModel)result;
		BoxDatabase boxDatabase = new BoxDatabase();
		BoxModel boxTo;
		ItemModel item;
		ItemBoxModel itemBox;
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		try
		{
			boxTo= boxDatabase.find(model.getToBoxId());
			if (account.getId() != boxTo.getOwner())
				throw new Exception("No allow to use the box.");
			item = boxDatabase.findItemByName(model.getItem());
			if (item == null)
			{
				item = new ItemModel();
				item.setName(model.getItem());
				boxDatabase.createItem(item);
				item = boxDatabase.findItemByName(model.getItem());
				itemBox = new ItemBoxModel();
				itemBox.setItemId(item.getId());
				itemBox.setBoxId(boxTo.getId());
				itemBox.setAmount(model.getAmount());
				boxDatabase.createItemBox(itemBox);
			}
			else
			{
				itemBox = boxDatabase.findItemBox(model.getToBoxId(), item.getId());
				if (itemBox == null)
				{
					itemBox = new ItemBoxModel();
					itemBox.setItemId(item.getId());
					itemBox.setBoxId(boxTo.getId());
					itemBox.setAmount(model.getAmount());
					boxDatabase.createItemBox(itemBox);
				}
				else
				{
					itemBox.addAmount(model.getAmount());
					boxDatabase.updateItemBox(itemBox);
				}
			}
			waybill.setAccountId(account.getId());
			waybill.setContents(item.getName() + " x " + model.getAmount());
			waybill.setFee(model.getAmount() * 10);
			waybill.setFrom("由公司出貨");
			waybill.setTo(boxTo.getLocation());
			waybill.setStatus(2);
			waybillDatabase.create(waybill);
		}
		catch (NullBoxException exception)
		{ return getErrorModelAndView(exception, exception.getMessage()); }
		catch (Exception exception)
		{ return getErrorModelAndView(exception, DB_ERROR);	}
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("delievrSuccessPage");
			return new ModelAndView("redirect:/" + page);
		}
	}
	
	@RequestMapping(value = "/itemToLocation", method = RequestMethod.POST)
	public ModelAndView deliverItemToLocation(@Valid ItemToLocationModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		Object result = sessionCheck(httpSession);
		if (result.getClass().equals(ModelAndView.class))
			return (ModelAndView) result;
		AccountModel account = (AccountModel)result;
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		try
		{
			WaybillModel waybill = new WaybillModel();
			waybill.setAccountId(account.getId());
			waybill.setContents(model.getContents());
			waybill.setFee(model.getContents().length() * 10);
			waybill.setFrom("由公司出貨");
			waybill.setTo(model.getLocation());
			waybill.setStatus(2);
			waybillDatabase.create(waybill);
		}
		catch (Exception exception)
		{ exception.printStackTrace(); return getErrorModelAndView(exception, DB_ERROR);	}
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("delievrSuccessPage");
			return new ModelAndView("redirect:/" + page);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getWaybillListPage() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			WaybillDatabase waybillDatabase = new WaybillDatabase();
			List<WaybillModel> list;
			try
			{
				list = waybillDatabase.listByAccount(account.getId());
			}
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("waybillListPage");
			ModelAndView view = new ModelAndView(page, "WaybillList", list);
			return view;
		}
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ModelAndView deleteWaybill(@PathVariable("id") int id) throws Exception
	{
		Object result = sessionCheck(httpSession);
		if (result.getClass().equals(ModelAndView.class))
			return (ModelAndView) result;
		AccountModel account = (AccountModel)result;
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill;
		try
		{
			waybill = waybillDatabase.find(id);
			if (waybill.getAccountId() != account.getId())
				throw new Exception("The waybill is not your.");
			waybillDatabase.delete(id);
		}
		catch (Exception exception)
		{ return getErrorModelAndView(exception, DB_ERROR);	}
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("delievrSuccessPage");
			return new ModelAndView("redirect:/" + page);
		}
	}
}
