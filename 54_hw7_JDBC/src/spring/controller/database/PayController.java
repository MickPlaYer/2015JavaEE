package spring.controller.database;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.validation.Valid;

import model.FeeModel;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.FeeBeenPaidException;
import exceptions.NullAccountException;
import service.fee.Fee;
import viewmodel.HelloModel;

@Controller()
@RequestMapping("/database")
public class PayController
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView getPayPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String payPage = (String)context.getBean("PAY_PAGE");
			return new ModelAndView(payPage);
		}
	}
	
	@RequestMapping(value = "/doPay", method = RequestMethod.POST)
	public ModelAndView doPay(@Valid HelloModel helloModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("PAY_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			
			FeeModel feeModel = new FeeModel();
			feeModel.setName(helloModel.getName());
			
			Fee fee = (Fee)context.getBean("fee");
			try	{ feeModel = payFee(feeModel, fee);	}
			catch (NullAccountException | FeeBeenPaidException exception)
			{
				return onDatabaseError(exception, errorPage, exception.getMessage());
			}
			catch (Exception e)	{ return onDatabaseError(e, errorPage, DB_ERROR); }

			return new ModelAndView(successPage, "FeeModel", feeModel);
		}
	}

	private FeeModel payFee(FeeModel feeModel, Fee fee) throws Exception, FeeBeenPaidException 
	{
		feeModel = fee.find(feeModel);
		int count = feeModel.getCount();
		if (count > 0)
		{
			feeModel.setCount(0);
			fee.update(feeModel);
		}
		else if (count == 0)
		{
			throw new FeeBeenPaidException();
		}
		feeModel.setCount(count);
		return feeModel;
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
