package spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

public class SpringController
{
	protected final String CONTEXT_FILE = "spring/controller/spring.xml";
	protected final String DB_ERROR = "error.database";
	protected final String ERROR_MODEL = "ErrorModel";
	protected ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	protected ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE);
	protected String errorPage;
	
	public SpringController(String errorPage)
	{
		this.errorPage = (String)context.getBean(errorPage);
	}
	
	protected ModelAndView getErrorModelAndView(Exception exception, String error)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(error) + " (" + exception.getMessage() + ")";
		feeErrors.add(new FieldError(className, error, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
	
	protected ModelAndView getErrorMessageModelAndView(Exception exception)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(exception.getMessage());
		feeErrors.add(new FieldError(className, exception.getMessage(), errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
	
	/*protected Object sessionCheck(HttpSession httpSession)
	{
		AccountModel account;
		try
		{ 
			AccountDatabase accountDatabase = new AccountDatabase();
			String name = (String)httpSession.getAttribute("name");
			String session = (String)httpSession.getAttribute("session");
			if (name == null || session == null)
				throw new SessionFailException();
			account = accountDatabase.findByName(name);
			if  (!account.getSession().equals(session))
				throw new SessionFailException();
		}
		catch (NullAccountException | SessionFailException exception)
		{ return getErrorModelAndView(exception, exception.getMessage()); }
		catch (Exception exception)
		{ return getErrorModelAndView(exception, DB_ERROR);	}
		return account;
	}*/
}
