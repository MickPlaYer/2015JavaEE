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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullBoxException;
import model.AccountModel;
import model.BoxModel;
import model.ItemAmountModel;
import service.account.BoxDatabase;
import viewmodel.BuyBoxModel;

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
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView mainPage() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("mainPage");
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView)result;
			AccountModel account = (AccountModel)result;
			return new ModelAndView(page, "Account", account);
		}
	}
	
	@RequestMapping(value = "/myAccount", method = RequestMethod.GET)
	public ModelAndView myAccount()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			String page = (String)context.getBean("myAccountPage");
			return new ModelAndView(page, "Account", account);
		}
	}
	
	@RequestMapping(value = "/myBox", method = RequestMethod.GET)
	public ModelAndView myBox() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			List<BoxModel> list = new ArrayList<BoxModel>();
			try
			{ 
				List<BoxModel> boxList = boxDatabase.listByOwner(account.getId());
				for(BoxModel box : boxList)
				{
					if (box.getDeadline() != null)
					{
						list.add(box);
					}
				}
			}
			catch (NullBoxException exception) { }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("myBoxPage");
			ModelAndView view = new ModelAndView(page);
			view.addObject("Account", account);
			view.addObject("BoxList", list);
			return view;
		}
	}
	
	@RequestMapping(value = "/buyBox", method = RequestMethod.GET)
	public ModelAndView buyBox() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			//Object result = sessionCheck(httpSession);
			//if (result.getClass().equals(ModelAndView.class))
			//	return (ModelAndView) result;
			String page = (String)context.getBean("buyBoxPage");
			return new ModelAndView(page);
		}
	}
	
	@RequestMapping(value = "/logistics", method = RequestMethod.GET)
	public ModelAndView logisticsPage() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("logisticsPage");
			return new ModelAndView(page);
		}
	}
	
	@RequestMapping(value = "/buyBox", method = RequestMethod.POST)
	public ModelAndView doBuyBox(@Valid BuyBoxModel model, BindingResult bindingResult) throws Exception
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
			try
			{
				String deadline = model.getDeadline();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				switch (deadline)
				{
					case "year":
						calendar.add(Calendar.YEAR, 1);
						break;
					case "helf-year":
						calendar.add(Calendar.MONTH, 6);
						break;
					case "month":
						calendar.add(Calendar.MONTH, 1);
						break;
					default:
						throw new Exception("Error deadline Options!");
				}
				box = new BoxModel();
				box.setOwner(account.getId());
				box.setName(model.getName());
				box.setLocation(model.getLocation());
				box.setDeadline(calendar.getTime());
				boxDatabase.create(box);
			}
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("myBoxPage");
			ModelAndView payPage = new ModelAndView("redirect:/" + page);
			/*ModelAndView payPage = new ModelAndView("redirect:/spring/main/test");
			payPage.addObject("token", "token");
			payPage.addObject("price", "1000");
			payPage.addObject("url", "xxx.xxx.xxx");*/
			return payPage;
		}
	}
}
