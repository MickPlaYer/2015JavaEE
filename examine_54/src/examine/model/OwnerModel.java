package examine.model;

public class OwnerModel
{
	private int id;
	private String name;
	private String email;
	private int cash;
	
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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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
}
