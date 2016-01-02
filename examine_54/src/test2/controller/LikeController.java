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

import test2.exceptions.NullUserException;
import test2.model.DatabaseModel;
import test2.service.database.NiceDatabase;
import test2.viewmodel.LikeModel;

@Controller()
public class LikeController
{
	private final String CONTEXT_FILE = "test2/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private ResourceBundle resource = ResourceBundle.getBundle("test2.resources.MessageDictionary");
	
	@RequestMapping(value = "/like", method = RequestMethod.GET)
	public ModelAndView getLikePage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String page = (String)context.getBean("LIKE_PAGE");
			return new ModelAndView(page);
		}
	}
	
	@RequestMapping(value = "/doLike", method = RequestMethod.POST)
	public ModelAndView doLike(@Valid LikeModel likeModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("LIKE_SUCCESS_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			NiceDatabase database = (NiceDatabase)context.getBean("NiceDatabase");
			DatabaseModel model;
			try
			{
				try
				{
					model = database.findByName(likeModel.getName());
					model.addCount(1);
					database.updateByName(model);
				}
				catch (NullUserException exception)
				{
					database.addNiceUser(likeModel.getName(), 1);
					model = database.findByName(likeModel.getName());
				}
			}
			catch (NullUserException exception) { return onDatabaseError(exception, errorPage, exception.getMessage()); }
			catch (Exception exception) { return onDatabaseError(exception, errorPage, DB_ERROR); }
			return new ModelAndView(successPage, "NiceModel", model);
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
