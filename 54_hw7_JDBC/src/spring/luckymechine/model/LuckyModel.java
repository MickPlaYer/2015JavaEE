package spring.luckymechine.model;

public class LuckyModel
{
	private int _id;
	private String _name;
	private int _dollars;
	private String _display;
	
	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public int getDollars()
	{
		return _dollars;
	}
	
	public void setDollars(int dollars)
	{
		_dollars = dollars;
	}
	
	public void addDollars(int dollars)
	{
		_dollars += dollars;
	}
	
	public String getDisplay()
	{
		return _display;
	}
	
	public void setDisplay(String display)
	{
		_display = display;
	}
}
