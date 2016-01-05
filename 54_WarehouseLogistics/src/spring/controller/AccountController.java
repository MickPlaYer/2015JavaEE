package spring.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import converter.MD5Converter;
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

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("registerPage");
			return new ModelAndView(page);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView doRegister(@Valid RegisterModel model, BindingResult bindingResult) throws Exception
	{	
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("registerSuccessPage");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			AccountDatabase accountDatabase = new AccountDatabase();
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
			catch (AccountExistException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			return new ModelAndView(successPage, "Register", model);
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@Valid LoginModel model, BindingResult bindingResult) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			String successPage = (String)context.getBean("loginSuccessPage");
			AccountDatabase accountDatabase = new AccountDatabase();
			AccountModel account;
			try
			{
				account = accountDatabase.findByName(model.getName());
				if (!account.getPassword().equals(model.getPassword()))
					throw new LoginFailException();
				String session = "MoNoGaTaRi" + model.getName() + new Date().toString();
				session = MD5Converter.convert(session);
				account.setSession(session);
				accountDatabase.update(account.getId(), account);
			}
			catch (NullAccountException | LoginFailException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			httpSession.setAttribute("name", account.getName());
			httpSession.setAttribute("session", account.getSession());
			return new ModelAndView(successPage, "Account", account);
		}
	}
}
