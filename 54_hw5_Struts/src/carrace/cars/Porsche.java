package carrace.cars;

import carrace.engines.Motor;

public class Porsche extends Car
{
	public Porsche()
	{
		super();
	}

	public Porsche(Motor motor)
	{
		super(motor);
	}

	public float getMaxSpeed()
	{
		return 24.0f;
	}
}
