package examine.controller;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import examine.exceptios.NullCarListException;
import examine.model.CarModel;
import examine.service.database.Database;

@Controller()
public class CarListController extends SpringController
{
	@RequestMapping(value = "/carList", method = RequestMethod.GET)
	public ModelAndView getCarListPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String carListPage = (String)context.getBean("carListPage");
			List<CarModel> carList;
			Database database = (Database)context.getBean("Database");
			try { carList = database.getCarlist(); }
			catch (NullCarListException exception)
			{ 
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(carListPage, "CarList", carList);
		}
	}
}
