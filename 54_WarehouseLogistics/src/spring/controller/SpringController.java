package spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
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
	protected SessionFactory sessionFactory;
	
	public SpringController(String errorPage)
	{
		this.errorPage = (String)context.getBean(errorPage);
	}

	protected void setupHibernateConfig(HttpSession httpSession)
	{
		sessionFactory = (SessionFactory)httpSession.getAttribute("SessionFactory");
		if (sessionFactory == null)
		{
			String CONFIG = (String)context.getBean("hibernateConfig");
			Configuration config = new Configuration().configure(CONFIG);
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
			httpSession.setAttribute("SessionFactory", sessionFactory);
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
	
	protected ModelAndView getErrorMessageModelAndView(Exception exception)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(exception.getMessage());
		feeErrors.add(new FieldError(className, exception.getMessage(), errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
