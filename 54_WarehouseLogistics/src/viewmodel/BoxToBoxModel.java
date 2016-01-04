package viewmodel;

import javax.validation.constraints.Pattern;

public class BoxToBoxModel
{
	private int fromBoxId;
	private int toBoxId;
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9_]+.{1,32}", message = "{error.userName.format}")
	private String item;
	private int amount;
	
	public int getFromBoxId()
	{
		return fromBoxId;
	}
	
	public void setFromBoxId(int fromBoxId)
	{
		this.fromBoxId = fromBoxId;
	}

	public int getToBoxId()
	{
		return toBoxId;
	}

	public void setToBoxId(int toBoxId)
	{
		this.toBoxId = toBoxId;
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
	
}
