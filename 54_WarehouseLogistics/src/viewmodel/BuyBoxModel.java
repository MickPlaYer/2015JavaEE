package viewmodel;

import org.hibernate.validator.constraints.NotBlank;

public class BuyBoxModel
{
	@NotBlank()
	private String name;
	@NotBlank()
	private String location;
	private String deadline;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getDeadline()
	{
		return deadline;
	}

	public void setDeadline(String deadline)
	{
		this.deadline = deadline;
	}
}
