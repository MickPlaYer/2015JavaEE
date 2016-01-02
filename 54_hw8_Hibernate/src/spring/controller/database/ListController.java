package spring.controller.database;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullListException;
import model.FeeModel;
import service.fee.Fee;

@Controller()
@RequestMapping("/database")
public class ListController
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/doList", method = RequestMethod.GET)
	public ModelAndView doQuery()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("LIST_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			List<FeeModel> feeModelList = new ArrayList<FeeModel>();
			Fee fee = (Fee)context.getBean("fee");
			try { feeModelList = fee.list(); }
			catch (NullListException nullListException)
			{
				return onDatabaseError(nullListException, errorPage, nullListException.getMessage());
			}
			catch (Exception e)	{ return onDatabaseError(e, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "FeeModelList", feeModelList);
		}
	}
	
	private ModelAndView onDatabaseError(Exception e, String errorPage, String error)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(error) + "<br>" + e.getMessage();
		feeErrors.add(new FieldError(className, error, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
