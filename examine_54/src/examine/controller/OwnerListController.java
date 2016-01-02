package examine.controller;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import examine.exceptios.NullOwnerListException;
import examine.model.OwnerModel;
import examine.service.database.Database;

@Controller()
public class OwnerListController extends SpringController
{
	@RequestMapping(value = "/ownerList", method = RequestMethod.GET)
	public ModelAndView getOwnerListPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String ownerListPage = (String)context.getBean("ownerListPage");
			List<OwnerModel> ownerList;
			Database database = (Database)context.getBean("Database");
			try { ownerList = database.getOwnerList(); }
			catch (NullOwnerListException exception)
			{ 
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(ownerListPage, "OwnerList", ownerList);
		}
	}
}
