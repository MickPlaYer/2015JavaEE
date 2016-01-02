package carrace.race;

import java.util.Random;

import carrace.cars.*;

public class Race implements RaceConstant
{
	private CarGarage _garage = new CarGarage();
	private Car[] _cars;
	private Car _pickedCar;
	private Car _winnerCar = null;
	private Runway _runways;
	private int _runs;
	private int _raceMile;
	private StringBuffer _raceDetail = new StringBuffer("-New Race!-");
	
	public Race(String carPick)
	{
		_cars = _garage.getCars();
		_pickedCar = _garage.pickCar(carPick);
		_raceMile = selectRaceMile();
		_runways = new Runway(_cars.length, BACKGROUND, RUN_WAY_LENGTH);
		_raceDetail.append("Total: " + _raceMile + "Mile-\n");
	}

	private int selectRaceMile()
	{
		Random rand = new Random();
		switch(rand.nextInt(RACE_TYPE_COUNT))
		{
		case 0:
			return SHORT_RACE;
		case 1:
			return MIDDLE_RACE;
		default:
			return LONG_RACE;
		}
	}
	
	public String getDetail()
	{
		return _raceDetail.toString();
	}
	
	public void Start()
	{
		Random rand = new Random();
		for (_runs = 0; _winnerCar == null; _runs++)
		{
			float windPower = rand.nextFloat();
			int fastestMile = (int)doCarsRun(windPower);
			drawSnapshot(fastestMile);
			_runways.drawRun(_runs, fastestMile, _raceDetail);
		}
		_raceDetail.append("The Winner is " + _winnerCar.getName() + " after " + _runs	+ " runs.");
	}

	private void drawSnapshot(int fastestMile)
	{
		for (int index = 0; index < _cars.length; index++)
		{
			Car car = _cars[index];
			int position = _runways.setCar(index, car, fastestMile);
			_runways.drawRunway(index, _raceDetail);
			_runways.clearCar(index, position);
		}
	}

	private float doCarsRun(float windPower)
	{
		float fastestMile = 0.0f;
		for (int carIndex = 0; carIndex < _cars.length; carIndex++)
		{
			_cars[carIndex].Run(windPower);
			float mile = _cars[carIndex].getMile();
			if (mile > _raceMile)
			{
				_winnerCar = _cars[carIndex];
			}
			if (mile > fastestMile)
				fastestMile = mile;
		}
		return fastestMile;
	}

	public String getReport()
	{
		String report = "Your pick is " + _pickedCar.getName() + ".\n";
		if (_winnerCar == _pickedCar)
		{
			report += "You win the race!";
		}
		else
		{
			report += "But the winner is " + _winnerCar.getName() + ".";
		}
		return report;
	}
}
