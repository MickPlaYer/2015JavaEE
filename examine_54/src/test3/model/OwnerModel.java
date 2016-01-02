package test3.model;

public class OwnerModel
{
	private int id;
	private String name;
	private int cash;
	private int asset;
	private int count;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getCash()
	{
		return cash;
	}

	public void setCash(int cash)
	{
		this.cash = cash;
	}

	public void addCash(int cash)
	{
		this.cash += cash;
	}
	
	public int getAsset()
	{
		return asset;
	}

	public void setAsset(int asset)
	{
		this.asset = asset;
	}

	public void addAsset(int asset)
	{
		this.asset += asset;
	}
	
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
	
	public void addCount(int count)
	{
		this.count += count;
	}
}
