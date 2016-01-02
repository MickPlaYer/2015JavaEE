package carrace.cars;

import java.util.Random;
import carrace.cars.*;
import carrace.engines.*;

public class CarGarage {
	private Car[] _cars;
	
	public CarGarage()
	{
		Motor[] motors = { new LowPower(1.2f), 
				new MediumPower(0.6f, 0.7f), 
				new HighPower(0.5f, 0.4f, 0.5f) };
		Car[] newCars = { new Porsche(motors[0]), new Buick(motors[1]), new Toyota(motors[2]) };
		_cars = newCars;
	}
	
	public String getCarDetail(String carPick)
	{
		Car car = pickCar(carPick);
		String detail = "The car's detail you picked.\n";
		detail += "Name: " + car.getName() + "\n";
		detail += "MaxSpeed: " + car.getMaxSpeed() + "\n";
		return detail;
	}

	public Car pickCar(String carPick)
	{
		carPick = carPick.toLowerCase();
		if (carPick.contains("1") || carPick.contains("p"))
		{
			return _cars[0];
		}
		else if (carPick.contains("2") || carPick.contains("b"))
		{
			return _cars[1];
		}
		else if (carPick.contains("3") || carPick.contains("t"))
		{
			return _cars[2];
		}
		Random rand = new Random();
		rand.setSeed(carPick.hashCode());
		return _cars[rand.nextInt(_cars.length)];
	}

	public Car[] getCars()
	{
		return _cars;
	}

}
