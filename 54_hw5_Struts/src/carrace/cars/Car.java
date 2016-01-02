package carrace.cars;

import carrace.engines.Engine;
import carrace.engines.Motor;

public class Car implements Engine 
{
	protected String _name;
	protected Motor _motor;
	private float _currentSpeed = 0.0f; 
	private float _currentMile = 0.0f;
	
	public Car()
	{
		_name = getClass().getSimpleName();
	}
	
	public Car(Motor motor)
	{
		this();
		_motor = motor;	
	}
	
	public String getName()
	{
		return _name;
	}

	public float getMaxSpeed()
	{
		return 10.0f;
	}

	public void Run(float windPower)
	{
		_currentSpeed += _motor.getPower() - windPower;
		if (_currentSpeed > getMaxSpeed())
			_currentSpeed = getMaxSpeed();
		_currentMile += _currentSpeed;
	}

	public String getSymbol()
	{
		return _name.substring(0, 1);
	}

	public float getMile()
	{
		return _currentMile;
	}
}
