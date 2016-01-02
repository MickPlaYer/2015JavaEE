package test3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import test3.exceptions.BuyOwnCarException;
import test3.exceptions.NotEnoughCashException;
import test3.exceptions.NullCarException;
import test3.exceptions.NullCarListException;
import test3.exceptions.NullOwnerException;
import test3.exceptions.PaymentUnderPriceException;
import test3.model.CarModel;
import test3.model.OwnerModel;
import test3.service.database.Database;
import test3.viewmodel.BuyCarModel;

@Controller()
public class BuyCarController extends SpringController
{
	@RequestMapping(value = "/buyCar", method = RequestMethod.GET)
	public ModelAndView getBuyCarPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String buyCarPage = (String)context.getBean("buyCarPage");
			return new ModelAndView(buyCarPage);
		}
	}
	
	@RequestMapping(value = "/doBuyCar", method = RequestMethod.POST)
	public ModelAndView doBuyCar(@Valid BuyCarModel buyCar, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			String buyCarSuccessPage = (String)context.getBean("buyCarSuccessPage");
			List<CarModel> carList;
			Database database = (Database)context.getBean("Database");
			try 
			{ 
				OwnerModel buyer = database.findOwner(buyCar.getBuyerId());
				CarModel car = database.findCar(buyCar.getCarId());
				OwnerModel seller = database.findOwner(car.getOwnerId());
				if (car.getOwnerId() == buyer.getId())
					throw new BuyOwnCarException();
				if (buyCar.getPayment() > buyer.getCash())
					throw new NotEnoughCashException();
				if (car.getPrice() > buyCar.getPayment())
					throw new PaymentUnderPriceException();
				database.buyCar(buyer, seller, car, buyCar.getPayment());
				carList = database.getOwnerCars(buyCar.getBuyerId());
			}
			catch (NullOwnerException | NullCarException | BuyOwnCarException | NotEnoughCashException | PaymentUnderPriceException | NullCarListException exception)
			{
				return getErrorModelAndView(exception, exception.getMessage());
			}
			catch (Exception exception)
			{ 
				return getErrorModelAndView(exception, DB_ERROR);
			}
			return new ModelAndView(buyCarSuccessPage, "CarList", carList);
		}
	}
}
