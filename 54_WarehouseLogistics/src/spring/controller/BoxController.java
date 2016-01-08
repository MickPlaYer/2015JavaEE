package spring.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.BoxDuplicateActivateException;
import exceptions.NullItemException;
import exceptions.WarehouseLogisticsException;
import model.AccountModel;
import model.BoxModel;
import model.ItemAmountModel;
import service.AccountService;
import service.BoxService;
import service.database.BoxDatabase;
import viewmodel.ModifyItemModel;

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
	
	@ModelAttribute("BeforeDo")
	public void beforeDo(HttpSession httpSession)
	{
		System.out.println("Box Controller Before Do");
		setupHibernateConfig(httpSession);
		System.out.println("Box Controller Before Do Done");
	}
	
	@RequestMapping(value = "/addItem/{boxId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView getAddItemWithIdPage(@PathVariable("boxId") int boxId, @PathVariable("itemId") int itemId) throws Exception
	{
		String page = (String)context.getBean("addItemPage");
		return getModifyItemPageWithItem(boxId, itemId, page);
	}
	
	@RequestMapping(value = "/addItem/{id}", method = RequestMethod.GET)
	public ModelAndView getAddItemPage(@PathVariable("id") int boxId) throws Exception
	{
		String page = (String)context.getBean("addItemPage");
		return getModifyItemPage(boxId, page);
	}
	
	@RequestMapping(value = "/addItem/{id}", method = RequestMethod.POST)
	public ModelAndView addItem(@Valid ModifyItemModel model, BindingResult bindingResult, @PathVariable("id") int boxId) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		boxService.addItemToBox(boxId, model);
		String page = (String)context.getBean("addItemSuccess");
		return new ModelAndView("redirect:/" + page + boxId);
	}
	
	@RequestMapping(value = "/removeItem/{boxId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView getRemoveItemWithIdPage(@PathVariable("boxId") int boxId, @PathVariable("itemId") int itemId) throws Exception
	{
		String page = (String)context.getBean("removeItemWithIdPage");
		return getModifyItemPageWithItem(boxId, itemId, page);
	}
	
	@RequestMapping(value = "/removeItem/{id}", method = RequestMethod.GET)
	public ModelAndView getRemoveItemPage(@PathVariable("id") int boxId) throws Exception
	{
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		List<ItemAmountModel> itemList = new ArrayList<ItemAmountModel>();
		try { itemList = boxService.getItemAmountList(boxId); }
		catch (NullItemException exception)	{ }
		String page = (String)context.getBean("removeItemPage");
		ModelAndView view = getModifyItemPage(boxId, page);
		view.addObject("ItemList", itemList);
		return view;
	}
	
	@RequestMapping(value = "/removeItem/{id}", method = RequestMethod.POST)
	public ModelAndView removeItem(@Valid ModifyItemModel model, BindingResult bindingResult, @PathVariable("id") int boxId) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		boxService.removeItemFromBox(boxId, model);
		String page = (String)context.getBean("removeItemSuccess");
		return new ModelAndView("redirect:/" + page + boxId);
	}
	
	private ModelAndView getModifyItemPageWithItem(int boxId, int itemId, String page) throws Exception
	{
		ModelAndView view = new ModelAndView(page);
		String readOnly = "readonly";
		BoxDatabase boxDatabase = new BoxDatabase(sessionFactory);
		ItemAmountModel item;
		item = boxDatabase.findItemAmount(boxId, itemId);
		view.addObject("Item", item);
		view.addObject("BoxId", boxId);
		view.addObject("ReadOnly", readOnly);
		return view;
	}

	private ModelAndView getModifyItemPage(int boxId, String page)
	{
		ModelAndView view = new ModelAndView(page);
		String readOnly = "";
		ItemAmountModel item = new ItemAmountModel();
		item.setName("");
		view.addObject("Item", item);			
		view.addObject("BoxId", boxId);			
		view.addObject("ReadOnly", readOnly);
		return view;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView getMyBox(@PathVariable("id") int boxId) throws Exception
	{
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		BoxModel box = boxService.getBoxWithoutCheck(boxId);
		List<ItemAmountModel> itemList = new ArrayList<ItemAmountModel>();
		try { itemList = boxService.getItemAmountList(box); }
		catch (NullItemException exception)	{ }
		String page = (String)context.getBean("myBoxItemPage");
		ModelAndView view = new ModelAndView(page);
		view.addObject("Box", box);
		view.addObject("ItemList", itemList);
		return view;
	}

	@RequestMapping(value = "/{id}/{hashCode}", method = RequestMethod.GET)
	public ModelAndView activateBox(@PathVariable("id") int boxId, @PathVariable("hashCode") String hashCode) throws Exception
	{
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		try { boxService.activateBox(boxId, hashCode); }
		catch (BoxDuplicateActivateException exception) { }
		String page = (String)context.getBean("removeItemSuccess");
		return new ModelAndView("redirect:/" + page + boxId);
	}
	
	@ExceptionHandler(WarehouseLogisticsException.class)
	public ModelAndView handleNullAccountException(WarehouseLogisticsException exception)
	{
		return getErrorMessageModelAndView(exception);
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception exception)
	{ 
		exception.printStackTrace();
		return getErrorModelAndView(exception, DB_ERROR);
	}
}
