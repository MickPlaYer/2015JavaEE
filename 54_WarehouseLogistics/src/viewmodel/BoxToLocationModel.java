package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class BoxToLocationModel
{
	private int fromBoxId;
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9_]+.{1,32}", message = "{error.userName.format}")
	private String item;
	private int amount;
	@NotBlank(message = "請填入目的地")
	private String location;
	
	public int getFromBoxId()
	{
		return fromBoxId;
	}
	
	public void setFromBoxId(int fromBoxId)
	{
		this.fromBoxId = fromBoxId;
	}

	public String getItem()
	{
		return item;
	}

	public void setItem(String item)
	{
		this.item = item;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
	
}
