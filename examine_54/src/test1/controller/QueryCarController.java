package test1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test1.exceptions.NullCarException;
import test1.model.DatabaseModel;
import test1.service.database.CarDatabase;
import test1.viewmodel.QueryCarModel;

@Controller()
public class QueryCarController
{
	private final String CONTEXT_FILE = "test1/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test1.resources.MessageDictionary");
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public ModelAndView getQueryPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String queryPage = (String)context.getBean("QUERY_PAGE");
			return new ModelAndView(queryPage);
		}
	}
	
	
	@RequestMapping(value = "/doQuery", method = RequestMethod.POST)
	public ModelAndView doQuery(@Valid QueryCarModel carModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("QUERY_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			CarDatabase carDatabase = (CarDatabase)context.getBean("CarDatabase");
			DatabaseModel model;

			try { model = carDatabase.findByName(carModel.getName()); }
			catch (NullCarException exception)	{ return onDatabaseError(exception, errorPage, exception.getMessage());	}
			catch (Exception exception)	{ return onDatabaseError(exception, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "CarModel", model);
		}
	}
	
	private ModelAndView onDatabaseError(Exception e, String errorPage, String error)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(error) + " (" + e.getMessage() + ")";
		feeErrors.add(new FieldError(className, error, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
