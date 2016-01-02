package examine.model;

public class CarModel
{
	private int id;
	private String name;
	private int price;
	private int ownerId;
	
	public CarModel()
	{

	}
	
	public CarModel(String name, int price, int ownerId)
	{
		this.name = name;
		this.price = price;
		this.ownerId = ownerId;
	}

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

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public int getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}
}
