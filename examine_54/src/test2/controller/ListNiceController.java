package test2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test2.exceptions.NullListException;
import test2.model.DatabaseModel;
import test2.service.database.NiceDatabase;

@Controller()
public class ListNiceController
{
	private final String CONTEXT_FILE = "test2/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test2.resources.MessageDictionary");
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView doList()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("LIST_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			List<DatabaseModel> list;
			NiceDatabase database = (NiceDatabase)context.getBean("NiceDatabase");
			try { list = database.listNiceUsers(); }
			catch (NullListException exception)	{ return onDatabaseError(exception, errorPage, exception.getMessage());	}
			catch (Exception exception)	{ return onDatabaseError(exception, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "NiceList", list);
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
