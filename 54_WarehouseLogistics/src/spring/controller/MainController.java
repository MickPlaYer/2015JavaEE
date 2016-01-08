package spring.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import exceptions.SessionFailException;
import exceptions.WarehouseLogisticsException;
import model.AccountModel;
import model.BoxModel;
import service.AccountService;
import service.BoxService;
import viewmodel.BuyBoxSalesman;
import webservice.requestmodel.PayModel;

@Controller()
@RequestMapping("/main")
public class MainController extends SpringController
{
	@Autowired
	private HttpSession httpSession;
	
	public MainController()
	{
		super("mainErrorPage");
	}
	
	@ModelAttribute("BeforeDo")
	public void beforeDo(HttpSession httpSession)
	{
		System.out.println("Box Controller Before Do");
		setupHibernateConfig(httpSession);
		System.out.println("Box Controller Before Do Done");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView mainPage() throws Exception
	{
		String name = (String)httpSession.getAttribute("name");
		if (name == null)
			throw new SessionFailException();
		String page = (String)context.getBean("mainPage");
		return new ModelAndView(page, "Name", name);
	}
	
	@RequestMapping(value = "/myAccount", method = RequestMethod.GET)
	public ModelAndView myAccount() throws Exception
	{
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		String page = (String)context.getBean("myAccountPage");
		return new ModelAndView(page, "Account", account);
	}
	
	@RequestMapping(value = "/myBox", method = RequestMethod.GET)
	public ModelAndView myBox() throws Exception
	{
		AccountModel account = new AccountService(sessionFactory).sessionCheck(httpSession);
		List<BoxModel> boxlist = new BoxService(account.getId(), sessionFactory).getActivatedBoxList();
		String page = (String)context.getBean("myBoxPage");
		ModelAndView view = new ModelAndView(page);
		view.addObject("Account", account);
		view.addObject("BoxList", boxlist);
		return view;
	}
	
	@RequestMapping(value = "/buyBox", method = RequestMethod.GET)
	public ModelAndView buyBox() throws Exception
	{
		String page = (String)context.getBean("buyBoxPage");
		return new ModelAndView(page);
	}
	
	@RequestMapping(value = "/logistics", method = RequestMethod.GET)
	public ModelAndView logisticsPage() throws Exception
	{
		String page = (String)context.getBean("logisticsPage");
		return new ModelAndView(page);
	}
	
	@RequestMapping(value = "/buyBox", method = RequestMethod.POST)
	public ModelAndView doBuyBox(@Valid BuyBoxSalesman salesman, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountService accountService = new AccountService(sessionFactory);
		AccountModel account = accountService.sessionCheck(httpSession);
		salesman = new BoxService(account.getId(), sessionFactory).buyNewBox(salesman);
		BoxModel box = salesman.getBox();
		PayModel payModel = salesman.getPay();
		payModel.setToken((String)context.getBean("bankToken"));
		payModel.setCallbackurl((String)context.getBean("warehouseURL") + "/" + box.getId() + "/" + box.getHashCode());
		RestTemplate restTemplate = new RestTemplate();
		String bankeURL = (String)context.getBean("bankURL") + "/auth";
		String payPage = restTemplate.postForObject(bankeURL, payModel, String.class);
		return new ModelAndView("redirect:" + payPage);
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
