package test3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test3.exceptions.NotEnoughCashException;
import test3.exceptions.NullCarListException;
import test3.exceptions.NullOwnerException;
import test3.model.CarModel;
import test3.model.OwnerModel;
import test3.service.database.Database;
import test3.viewmodel.BuildCarModel;

@Controller()
public class BuildCarController extends SpringController
{
	@RequestMapping(value = "/buildCar", method = RequestMethod.GET)
	public ModelAndView getBuildCarPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String buildCarPage = (String)context.getBean("buildCarPage");
			return new ModelAndView(buildCarPage);
		}
	}
	
	@RequestMapping(value = "/doBuildCar", method = RequestMethod.POST)
	public ModelAndView doBuildCar(@Valid BuildCarModel buildCar, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			String buildCarSuccessPage = (String)context.getBean("buildCarSuccessPage");
			List<CarModel> carList;
			Database database = (Database)context.getBean("Database");
			try 
			{ 
				OwnerModel builder = database.findOwner(buildCar.getBuilderId());
				if (builder.getCash() < buildCar.getCarPrice())
					throw new NotEnoughCashException();
				database.buildCar(builder, buildCar.getCarName(), buildCar.getCarPrice());
				carList = database.getOwnerCars(buildCar.getBuilderId());
			}
			catch (NullOwnerException | NotEnoughCashException | NullCarListException exception)
			{
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(buildCarSuccessPage, "CarList", carList);
		}
	}
}
