package spring.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullBoxException;
import exceptions.PageNotFoundException;
import exceptions.SameLocationException;
import exceptions.WarehouseLogisticsException;
import exceptions.WaybillPrivilegeException;
import model.AccountModel;
import model.BoxModel;
import model.ItemBoxModel;
import model.ItemModel;
import model.WaybillModel;
import service.AccountService;
import service.BoxService;
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
		AccountModel account = new AccountService().sessionCheck(httpSession);
		String page;
		try { page = (String)context.getBean(method + "Page"); }
		catch (Exception exception){ throw new PageNotFoundException(); }
		ModelAndView view = new ModelAndView(page);
		if (method.equals("itemToLocation"))
			return view;
		BoxService boxService = new BoxService(account.getId());
		List<BoxModel> boxList = boxService.getUseableBoxList();
		List<ItemModel> itemList = boxService.getItemList();
		view.addObject("BoxList", boxList);
		view.addObject("ItemList", itemList);
		return view;
	}
	
	@RequestMapping(value = "/boxToBox/{boxId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView boxToBoxWithId(@PathVariable("boxId") int boxId, @PathVariable("itemId") int itemId) throws Exception
	{
		AccountModel account = new AccountService().sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId());
		BoxModel box = boxService.getBox(boxId);
		ItemModel item = boxService.findItem(itemId);
		ItemBoxModel itemBox = boxService.findItemBox(box.getId(), item.getId());
		List<BoxModel> boxList = boxService.getUseableBoxList();
		String page = (String)context.getBean("boxToBoxWithIdPage");
		ModelAndView view = new ModelAndView(page);
		view.addObject("Box", box);
		view.addObject("Item", item);
		view.addObject("ItemBox", itemBox);
		view.addObject("BoxList", boxList);
		return view;
	}
	
	@RequestMapping(value = "/boxToLocation/{boxId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView boxToLocationWithId(@PathVariable("boxId") int boxId, @PathVariable("itemId") int itemId) throws Exception
	{
		AccountModel account = new AccountService().sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId());
		BoxModel box = boxService.getBox(boxId);
		ItemModel item = boxService.findItem(itemId);
		ItemBoxModel itemBox = boxService.findItemBox(box.getId(), item.getId());
		String page = (String)context.getBean("boxToLocationWithIdPage");
		ModelAndView view = new ModelAndView(page);
		view.addObject("Box", box);
		view.addObject("Item", item);
		view.addObject("ItemBox", itemBox);
		return view;
	}
	
	@RequestMapping(value = "/boxToBox", method = RequestMethod.POST)
	public ModelAndView deliverBoxToBox(@Valid BoxToBoxModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService().sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId());
		BoxModel boxFrom = boxService.getBox(model.getFromBoxId());
		BoxModel boxTo = boxService.getBox(model.getToBoxId());
		ItemModel item = boxService.findItemByName(model.getItem());
		if (model.getFromBoxId() == model.getToBoxId())
			throw new SameLocationException();
		boxService.deliverBoxToBox(model, item);
		
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		waybill.setAccountId(account.getId());
		waybill.setContents(item.getName() + " x " + model.getAmount());
		waybill.setFee(model.getAmount() * 10);
		waybill.setFrom(boxFrom.getLocation());
		waybill.setTo(boxTo.getLocation());
		waybill.setStatus(2);
		waybillDatabase.create(waybill);
		
		String page = (String)context.getBean("delievrSuccessPage");
		return new ModelAndView("redirect:/" + page);
	}
	
	@RequestMapping(value = "/boxToLocation", method = RequestMethod.POST)
	public ModelAndView deliverBoxToLocation(@Valid BoxToLocationModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService().sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId());
		boxService.deliverBoxToLocation(model.getFromBoxId(), model);
		BoxModel boxFrom = boxService.getBox(model.getFromBoxId());
		ItemModel item = boxService.findItemByName(model.getItem());
			
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		waybill.setAccountId(account.getId());
		waybill.setContents(item.getName() + " x " + model.getAmount());
		waybill.setFee(model.getAmount() * 10);
		waybill.setFrom(boxFrom.getLocation());
		waybill.setTo(model.getLocation());
		waybill.setStatus(2);
		waybillDatabase.create(waybill);

		String page = (String)context.getBean("delievrSuccessPage");
		return new ModelAndView("redirect:/" + page);
	}
	
	@RequestMapping(value = "/itemToBox", method = RequestMethod.POST)
	public ModelAndView deliverItemToBox(@Valid ItemToBoxModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService().sessionCheck(httpSession);
		BoxService boxService = new BoxService(account.getId());
		boxService.deliverItemToBox(model.getToBoxId(), model);
		BoxModel boxTo = boxService.getBox(model.getToBoxId());
		ItemModel item = boxService.findItemByName(model.getItem());
		
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		waybill.setAccountId(account.getId());
		waybill.setContents(item.getName() + " x " + model.getAmount());
		waybill.setFee(model.getAmount() * 10);
		waybill.setFrom("由公司出貨");
		waybill.setTo(boxTo.getLocation());
		waybill.setStatus(2);
		waybillDatabase.create(waybill);
		
		String page = (String)context.getBean("delievrSuccessPage");
		return new ModelAndView("redirect:/" + page);
	}
	
	@RequestMapping(value = "/itemToLocation", method = RequestMethod.POST)
	public ModelAndView deliverItemToLocation(@Valid ItemToLocationModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountModel account = new AccountService().sessionCheck(httpSession);
		
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		waybill.setAccountId(account.getId());
		waybill.setContents(model.getContents());
		waybill.setFee(model.getContents().length() * 10);
		waybill.setFrom("由公司出貨");
		waybill.setTo(model.getLocation());
		waybill.setStatus(2);
		waybillDatabase.create(waybill);
		
		String page = (String)context.getBean("delievrSuccessPage");
		return new ModelAndView("redirect:/" + page);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getWaybillListPage() throws Exception
	{
		AccountModel account = new AccountService().sessionCheck(httpSession);
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		List<WaybillModel> list;
		list = waybillDatabase.listByAccount(account.getId());
		String page = (String)context.getBean("waybillListPage");
		ModelAndView view = new ModelAndView(page, "WaybillList", list);
		return view;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ModelAndView deleteWaybill(@PathVariable("id") int id) throws Exception
	{
		AccountModel account = new AccountService().sessionCheck(httpSession);
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill;
		waybill = waybillDatabase.find(id);
		if (waybill.getAccountId() != account.getId())
			throw new WaybillPrivilegeException();
		waybillDatabase.delete(id);
		String page = (String)context.getBean("delievrSuccessPage");
		return new ModelAndView("redirect:/" + page);
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
