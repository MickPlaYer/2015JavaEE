package carrace.engines;

public class LowPower implements Motor
{
	private float _volume;

	public LowPower(float volume)
	{
		_volume = volume;
	}

	public float getPower()
	{
		return _volume;
	}
}
