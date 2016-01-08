package spring.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import converter.MD5Converter;
import exceptions.WarehouseLogisticsException;
import exceptions.AccountExistException;
import exceptions.LoginFailException;
import exceptions.NullAccountException;
import model.AccountModel;
import service.database.AccountDatabase;
import viewmodel.LoginModel;
import viewmodel.RegisterModel;

@Controller()
@RequestMapping("/account")
public class AccountController extends SpringController
{
	@Autowired
	private HttpSession httpSession;

	public AccountController()
	{
		super("errorPage");
	}
	
	@ModelAttribute("BeforeDo")
	public void beforeDo(HttpSession httpSession)
	{
		System.out.println("Box Controller Before Do");
		setupHibernateConfig(httpSession);
		System.out.println("Box Controller Before Do Done");
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register()
	{
		String page = (String)context.getBean("registerPage");
		return new ModelAndView(page);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView doRegister(@Valid RegisterModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountDatabase accountDatabase = new AccountDatabase(sessionFactory);
		AccountModel account;
		try
		{
			account = accountDatabase.findByName(model.getName());
			throw new AccountExistException();
		}
		catch (NullAccountException exception)
		{
			String token = "BaKeMoNo" + model.getName() + new Date().toString();
			token = MD5Converter.convert(token);
			account = new AccountModel();
			account.setName(model.getName());
			account.setPassword(model.getPassword());
			account.setToken(token);
			accountDatabase.create(account);
		}
		String successPage = (String)context.getBean("registerSuccessPage");
		return new ModelAndView(successPage, "Register", model);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@Valid LoginModel model, BindingResult bindingResult) throws Exception
	{
		if (bindingResult.hasErrors())
			return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
		AccountDatabase accountDatabase = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabase.findByName(model.getName());
		if (!account.getPassword().equals(model.getPassword()))
			throw new LoginFailException();
		String session = "MoNoGaTaRi" + model.getName() + new Date().toString();
		session = MD5Converter.convert(session);
		account.setSession(session);
		accountDatabase.update(account.getId(), account);
		httpSession.setAttribute("name", account.getName());
		httpSession.setAttribute("session", account.getSession());
		String successPage = (String)context.getBean("loginSuccessPage");
		return new ModelAndView(successPage, "Account", account);
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
