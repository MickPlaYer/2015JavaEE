package carrace.cars;

import carrace.engines.Motor;

public class Buick extends Car
{
	public Buick()
	{
		super();
	}
	
	public Buick(Motor motor)
	{
		super(motor);
	}
	
	public float getMaxSpeed()
	{
		return 18.0f;
	}
}
