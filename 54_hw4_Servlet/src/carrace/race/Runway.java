package carrace.race;

import carrace.cars.Car;

public class Runway
{
	private StringBuffer _runways[];
	private String _line;
	private String _background;
	private int _length;
	
	public Runway(int size, String background, int length)
	{
		_length = length;
		_runways = new StringBuffer[size];
		_background = background;
		for (int i = 0; i < size; i++)
		{
			_runways[i] = new StringBuffer(length);
			for (int j = 0; j < length; j++)
			{
				_runways[i].replace(j, j + 1, background);
			}
		}
		_line = new String();
		for (int i = 0; i < length; i++)
		{
			_line += background;
		}
	}

	public int setCar(int index, Car car, int fastestMile)
	{
		int mile = (int)car.getMile();
		int position = -1;
		int lowestMile = fastestMile - _length;
		if (mile > lowestMile)
		{
			position = mile - lowestMile;
			_runways[index].replace(position - 1, position, car.getSymbol());
		}
		return position;
	}

	public void clearCar(int i, int position)
	{
		if (position == -1)
			return;
		_runways[i].replace(position - 1, position, _background);
	}

	public void drawRunway(int index, StringBuffer drawTo)
	{
		drawTo.append(_runways[index] + "\n");
	}

	public void drawLine(StringBuffer drawTo)
	{
		drawTo.append(_line + "\n");
	}
	
	public void drawRun(int runs, int mile, StringBuffer drawTo)
	{
		drawTo.append("　　RUN:" + (runs + 1) + "       Mile: " + String.format("%3d", mile) + "^\n");
	}
}
