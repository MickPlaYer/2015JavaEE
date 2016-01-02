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
import model.ItemAmountModel;
import service.account.BoxDatabase;
import viewmodel.AddItemModel;

@Controller()
@RequestMapping("/box")
public class BoxController extends SpringController
{
	@Autowired
	private HttpSession httpSession;
	
	public BoxController()
	{
		super("boxErrorPage");
	}

	@RequestMapping(value = "/addItem/{id}/{iid}", method = RequestMethod.GET)
	public ModelAndView getAddItemPageWithId(@PathVariable("id") int id, @PathVariable("iid") int iid) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("addItemPage");
			return getModifyItemPageWithItem(id, iid, page);
		}
	}
	
	@RequestMapping(value = "/addItem/{id}", method = RequestMethod.GET)
	public ModelAndView getAddItemPage(@PathVariable("id") int id) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("addItemPage");
			return getModifyItemPage(id, page);
		}
	}
	
	@RequestMapping(value = "/addItem/{id}", method = RequestMethod.POST)
	public ModelAndView addItem(@Valid AddItemModel model, BindingResult bindingResult, @PathVariable("id") int id) throws Exception
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
			BoxModel box;
			ItemModel item;
			ItemBoxModel itemBox;
			try
			{
				box = boxDatabase.find(id);
				if (account.getId() != box.getOwner())
					throw new Exception("No allow to use the box.");
				item = boxDatabase.findItemByName(model.getName());
				if (item == null)
				{
					item = new ItemModel();
					item.setName(model.getName());
					boxDatabase.createItem(item);
					item = boxDatabase.findItemByName(model.getName());
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
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("addItemSuccess");
			return new ModelAndView("redirect:/" + page + id);
		}
	}
	
	@RequestMapping(value = "/removeItem/{id}/{iid}", method = RequestMethod.GET)
	public ModelAndView getRemoveItemPageWithId(@PathVariable("id") int id, @PathVariable("iid") int iid) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			BoxDatabase boxDatabase = new BoxDatabase();
			BoxModel box;
			List<ItemAmountModel> itemList;
			try
			{
				box = boxDatabase.find(id);
				itemList = boxDatabase.listItemAmount(box);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("removeItemPage");
			ModelAndView view = getModifyItemPageWithItem(id, iid, page);
			view.addObject("ItemList", itemList);
			return view;
		}
	}
	
	@RequestMapping(value = "/removeItem/{id}", method = RequestMethod.GET)
	public ModelAndView getRemoveItemPage(@PathVariable("id") int id) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("removeItemPage");
			return getModifyItemPage(id, page);
		}
	}
	
	@RequestMapping(value = "/removeItem/{id}", method = RequestMethod.POST)
	public ModelAndView removeItem(@Valid AddItemModel model, BindingResult bindingResult, @PathVariable("id") int id) throws Exception
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
			BoxModel box;
			ItemModel item;
			ItemBoxModel itemBox;
			try
			{
				box = boxDatabase.find(id);
				if (account.getId() != box.getOwner())
					throw new Exception("No allow to use the box.");
				item = boxDatabase.findItemByName(model.getName());
				if (item == null)
					throw new Exception("Item not exist.");
				itemBox = boxDatabase.findItemBox(id, item.getId());
				if (itemBox == null)
					throw new Exception("Item not exist in the box.");
				if (model.getAmount() > itemBox.getAmount())
					throw new Exception("Item not enough in the box.");
				itemBox.addAmount(-model.getAmount());
				if (itemBox.getAmount() == 0)
					boxDatabase.deleteItemBox(itemBox);
				else if (itemBox.getAmount() > 0)
					boxDatabase.updateItemBox(itemBox);
				else
					throw new Exception("BoxController Error");
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("removeItemSuccess");
			return new ModelAndView("redirect:/" + page + id);
		}
	}
	
	private ModelAndView getModifyItemPageWithItem(int id, int iid, String page) throws Exception
	{
		ModelAndView view = new ModelAndView(page);
		String readOnly = "readonly";
		BoxDatabase boxDatabase = new BoxDatabase();
		ItemAmountModel item;
		try
		{
			item = boxDatabase.findItemAmount(id, iid);
			view.addObject("Item", item);
		}
		catch (Exception exception)
		{ return getErrorModelAndView(exception, DB_ERROR);	}
		view.addObject("BoxId", id);
		view.addObject("ReadOnly", readOnly);
		return view;
	}

	private ModelAndView getModifyItemPage(int id, String page)
	{
		ModelAndView view = new ModelAndView(page);
		String readOnly = "";
		ItemAmountModel item = new ItemAmountModel();
		item.setName("");
		view.addObject("Item", item);			
		view.addObject("BoxId", id);			
		view.addObject("ReadOnly", readOnly);
		return view;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView getMyBox(@PathVariable("id") int id) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			List<ItemAmountModel> itemList = new ArrayList<ItemAmountModel>();
			BoxModel box;
			try
			{
				box = boxDatabase.find(id);
				if (account.getId() != box.getOwner())
					throw new Exception("No allow to view the box.");
				itemList = boxDatabase.listItemAmount(box);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("myBoxItemPage");
			ModelAndView view = new ModelAndView(page);
			view.addObject("Box", box);
			view.addObject("ItemList", itemList);
			return view;
		}
	}
}
