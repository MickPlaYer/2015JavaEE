package carrace.engines;

public class MediumPower implements Motor
{
	private float _volume;
	private float _material;
	
	public MediumPower(float volume, float material)
	{
		_volume = volume;
		_material = material;
	}

	public float getPower()
	{
		return _volume + _material;
	}
}
