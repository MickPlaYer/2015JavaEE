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
import test1.service.mail.Mail;
import test1.service.message.Message;
import test1.viewmodel.SellCarModel;

@Controller()
public class SellCarController
{
	private final String CONTEXT_FILE = "test1/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test1.resources.MessageDictionary");
	
	@RequestMapping(value = "/sell", method = RequestMethod.GET)
	public ModelAndView getQueryPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String sellPage = (String)context.getBean("SELL_PAGE");
			return new ModelAndView(sellPage);
		}
	}
	
	
	@RequestMapping(value = "/doSell", method = RequestMethod.POST)
	public ModelAndView doQuery(@Valid SellCarModel carModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("SELL_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			CarDatabase carDatabase = (CarDatabase)context.getBean("CarDatabase");
			Message message = (Message)context.getBean("Message");
			Mail mail = (Mail)context.getBean("Mail");
			DatabaseModel model;
			try
			{ 
				model = carDatabase.findByName(carModel.getName());
				model.addCount(-1);
				if (model.getCount() == 0)
					carDatabase.deleteByName(model);
				else
					carDatabase.updateByName(model);
			}
			catch (NullCarException exception)	{ return onDatabaseError(exception, errorPage, exception.getMessage());	}
			catch (Exception exception)	{ return onDatabaseError(exception, errorPage, DB_ERROR); }
			String result = message.doMessage(carModel);
			String stamp = mail.sendMail(result);
			ModelAndView modelAndView = new ModelAndView(successPage);
			modelAndView.addObject("CarModel", carModel);
			modelAndView.addObject("Stamp", stamp);
			return modelAndView;
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
