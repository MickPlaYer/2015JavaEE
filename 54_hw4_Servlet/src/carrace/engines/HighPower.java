package carrace.engines;

public class HighPower implements Motor
{
	private float _volume;
	private float _material;
	private float _technology;

	public HighPower(float volume, float material, float technology)
	{
		_volume = volume;
		_material = material;
		_technology = technology;
	}

	public float getPower()
	{
		return _volume + _material + _technology;
	}
}
