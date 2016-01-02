package test1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test1.exceptions.NullListException;
import test1.model.DatabaseModel;
import test1.service.database.CarDatabase;

@Controller()
public class ListCarController
{
	private final String CONTEXT_FILE = "test1/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test1.resources.MessageDictionary");
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView doList()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("LIST_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			List<DatabaseModel> carList;
			CarDatabase carDatabase = (CarDatabase)context.getBean("CarDatabase");
			try { carList = carDatabase.listCars(); }
			catch (NullListException exception)	{ return onDatabaseError(exception, errorPage, exception.getMessage());	}
			catch (Exception exception)	{ return onDatabaseError(exception, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "CarList", carList);
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
