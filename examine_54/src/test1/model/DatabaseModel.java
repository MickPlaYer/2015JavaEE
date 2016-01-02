package test1.model;

public class DatabaseModel
{
	private int _id;
	private String _name;
	private int _count;
	
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

	public int getCount()
	{
		return _count;
	}

	public void setCount(int count)
	{
		_count = count;
	}

	public void addCount(int count)
	{
		_count += count;
	}
}
