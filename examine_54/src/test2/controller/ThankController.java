package test2.controller;

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

import test2.service.mail.Mail;
import test2.exceptions.NotEnoughtException;
import test2.exceptions.NullUserException;
import test2.model.DatabaseModel;
import test2.service.database.NiceDatabase;
import test2.service.message.Message;
import test2.viewmodel.ThankModel;

@Controller()
public class ThankController
{
	private final String CONTEXT_FILE = "test2/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test2.resources.MessageDictionary");
	
	@RequestMapping(value = "/thank", method = RequestMethod.GET)
	public ModelAndView getThankPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("THANK_PAGE");
			return new ModelAndView(page);
		}
	}
	
	@RequestMapping(value = "/doThank", method = RequestMethod.POST)
	public ModelAndView doThank(@Valid ThankModel thankModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("THANK_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			NiceDatabase database = (NiceDatabase)context.getBean("NiceDatabase");
			Message message = (Message)context.getBean("Message");
			Mail mail = (Mail)context.getBean("Mail");
			DatabaseModel model;

			try 
			{
				model = database.findByName(thankModel.getName());
				model.addCount(-thankModel.getCount());
				if (model.getCount() < 0)
					throw new NotEnoughtException();
				else if (model.getCount() == 0)
					database.deleteByName(model);
				else
					database.updateByName(model);
			}
			catch (NotEnoughtException | NullUserException exception)	{ return onDatabaseError(exception, errorPage, exception.getMessage());	}
			catch (Exception exception)	{ return onDatabaseError(exception, errorPage, DB_ERROR); }
			model.setCount(thankModel.getCount());
			String result = message.doMessage(thankModel);
			String stamp = mail.sendMail(result);
			ModelAndView modelAndView = new ModelAndView(successPage);
			modelAndView.addObject("NiceModel", model);
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
