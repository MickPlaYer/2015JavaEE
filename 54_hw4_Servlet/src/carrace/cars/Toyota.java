package carrace.cars;

import carrace.engines.Motor;

public class Toyota extends Car
{
	public Toyota()
	{
		super();
	}

	public Toyota(Motor motor)
	{
		super(motor);
	}

	public float getMaxSpeed()
	{
		return 12.0f;
	}
}
