package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ModifyItemModel
{
	@NotBlank(message = "{error.userName.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9_]+.{1,32}", message = "{error.userName.format}")
	private String name;
	private int amount;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
