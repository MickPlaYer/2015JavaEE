package test3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test3.exceptions.NullCarListException;
import test3.exceptions.NullOwnerException;
import test3.service.database.Database;
import test3.viewmodel.QueryModel;
import test3.model.CarModel;

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
	
	@RequestMapping(value = "/myCars", method = RequestMethod.GET)
	public ModelAndView getQueryCarsPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String queryCarsPage = (String)context.getBean("queryCarsPage");
			return new ModelAndView(queryCarsPage);
		}
	}
	
	@RequestMapping(value = "/queryCars", method = RequestMethod.POST)
	public ModelAndView queryCars(@Valid QueryModel queryModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			String myCarsPage = (String)context.getBean("myCarsPage");
			List<CarModel> carList;
			Database database = (Database)context.getBean("Database");
			try 
			{ 
				database.findOwner(queryModel.getId());
				carList = database.getOwnerCars(queryModel.getId());
			}
			catch (NullCarListException | NullOwnerException exception)
			{
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(myCarsPage, "CarList", carList);
		}
	}
}
