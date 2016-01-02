package spring.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullBoxException;
import model.AccountModel;
import model.BoxModel;
import service.account.BoxDatabase;
import viewmodel.BoxToBoxModel;

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
	public ModelAndView boxToBox(@PathVariable("method") String method) throws Exception
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
			try
			{ 
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
			return view;
		}
	}
	
	@RequestMapping(value = "/boxToBox/{bid}/{iid}", method = RequestMethod.GET)
	public ModelAndView boxToBoxWithId(@PathVariable("bid") int boxId, @PathVariable("iid") int itemId) throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{	
			// Here to continue.
			String page = (String)context.getBean("BoxToBoxWithIdPage");
			ModelAndView view = new ModelAndView(page);
			return view;
		}
	}
	
	@RequestMapping(value = "/boxToBox", method = RequestMethod.POST)
	public String deliverBoxToBox(BoxToBoxModel model) throws Exception
	{
		String post = "From=" + model.getFromBoxId() + " To=" + model.getToBoxId() + " Item=" + model.getItem() + " Amount=" + model.getAmount();
		System.out.println("\n" + post + "\n");
		return post;
	}
	
	/*@RequestMapping(value = "/boxToLocation", method = RequestMethod.GET)
	public ModelAndView boxToLocation() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Object result = sessionCheck(httpSession);
			if (result.getClass().equals(ModelAndView.class))
				return (ModelAndView) result;
			AccountModel account = (AccountModel)result;
			BoxDatabase boxDatabase = new BoxDatabase();
			List<BoxModel> boxList = new ArrayList<BoxModel>();
			try
			{ 
				List<BoxModel> list = boxDatabase.listByOwner(account.getId());
				for(BoxModel box : list)
					if (box.getHashCode() == null)
						boxList.add(box);
			}
			catch (NullBoxException exception)
			{ return getErrorModelAndView(exception, exception.getMessage()); }
			catch (Exception exception)
			{ return getErrorModelAndView(exception, DB_ERROR);	}
			String page = (String)context.getBean("boxToLocationPage");
			ModelAndView view = new ModelAndView(page);
			view.addObject("BoxList", boxList);
			return view;
		}
	}*/
}
