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
import service.mail.Mail;
import service.message.Message;

import viewmodel.HelloModel;
import viewmodel.MessageModel;

@Controller()
@RequestMapping("/database")
public class HelloController
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView getHelloPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String helloPage = (String)context.getBean("HELLO_PAGE");
			return new ModelAndView(helloPage);
		}
	}
	
	@RequestMapping(value = "/doHello", method = RequestMethod.POST)
	public ModelAndView doHello(@Valid HelloModel helloModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			
			MessageModel messageModel = (MessageModel)context.getBean("messageModel");
			FeeModel feeModel = new FeeModel();
			
			Message message = (Message)context.getBean("message");
			String name = helloModel.getName();
			String result = message.doMessage(name);
			
			Mail mail = (Mail)context.getBean("mail");
			String stamp = mail.sendMail(result);
			
			Fee fee = (Fee)context.getBean("fee");
			feeModel.setName(name);
			try { feeModel = doFee(fee, feeModel, errorPage); }
			catch (Exception e)	{ return onDatabaseError(e, errorPage); }

			messageModel.setResults(result, stamp, feeModel.getCount());
			return new ModelAndView(successPage, "MessageModel", messageModel);
		}
	}
	
	private FeeModel doFee(Fee fee, FeeModel feeModel, String errorPage) throws Exception
	{
		try
		{
			feeModel = fee.find(feeModel);
			if (feeModel.getCount() >= 0)
			{
				feeModel.addCount();
				fee.update(feeModel);
			}
		}
		catch (NullAccountException nullAccountException)
		{
			feeModel.setCount(1);
			fee.create(feeModel);
		}
		return feeModel;
	}
	
	private ModelAndView onDatabaseError(Exception e, String errorPage)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(DB_ERROR) + "<br>" + e.getMessage();
		feeErrors.add(new FieldError(className, DB_ERROR, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
