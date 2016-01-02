package test3.controller;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test3.exceptions.NullOwnerException;
import test3.model.OwnerModel;
import test3.service.database.Database;
import test3.viewmodel.QueryModel;

@Controller()
public class AccountController extends SpringController
{
	@RequestMapping(value = "/myAccount", method = RequestMethod.GET)
	public ModelAndView getQueryAccountPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String queryAccountPage = (String)context.getBean("queryAccountPage");
			return new ModelAndView(queryAccountPage);
		}
	}
	
	@RequestMapping(value = "/queryAccount", method = RequestMethod.POST)
	public ModelAndView queryAccount(@Valid QueryModel queryModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String myAccountPage = (String)context.getBean("myAccountPage");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			OwnerModel model;
			Database database = (Database)context.getBean("Database");
			try { model = database.findOwner(queryModel.getId()); }
			catch (NullOwnerException exception)
			{ 
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(myAccountPage, "Owner", model);
		}
	}
}
