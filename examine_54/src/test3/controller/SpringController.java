package test3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

public class SpringController
{
	protected final String CONTEXT_FILE = "test3/controller/spring.xml";
	protected final String DB_ERROR = "error.database";
	protected final String ERROR_MODEL = "ErrorModel";
	protected ResourceBundle resource = ResourceBundle.getBundle("test3.resources.MessageDictionary");
	protected String errorPage;
	
	public SpringController()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			errorPage = (String)context.getBean("errorPage");
		}
	}
	
	protected ModelAndView getErrorModelAndView(Exception exception, String error)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(error) + " (" + exception.getMessage() + ")";
		feeErrors.add(new FieldError(className, error, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
