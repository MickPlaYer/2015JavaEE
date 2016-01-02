package spring.controller.database;

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

import exceptions.NullAccountException;
import model.FeeModel;
import service.fee.Fee;

import viewmodel.HelloModel;

@Controller()
@RequestMapping("/database")
public class QueryController
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
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
	public ModelAndView doQuery(@Valid HelloModel helloModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("QUERY_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			
			FeeModel feeModel = new FeeModel();
			feeModel.setName(helloModel.getName());

			Fee fee = (Fee)context.getBean("fee");
			try { feeModel = fee.find(feeModel); }
			catch (NullAccountException nullAccountException)
			{
				return onDatabaseError(nullAccountException, errorPage, nullAccountException.getMessage());
			}
			catch (Exception e)	{ return onDatabaseError(e, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "FeeModel", feeModel);
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
