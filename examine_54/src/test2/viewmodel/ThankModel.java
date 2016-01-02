package test2.viewmodel;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ThankModel
{
	private String _name;
	private int _count;
	
	@Length(max = 50, message = "{error.name.tooLong}")
	@NotEmpty(message = "{error.name.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF]+", message = "{error.name.charOnly}")
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	@Min(value=1, message="{error.count.atLeastOne}")
	public int getCount()
	{
		return _count;
	}
	
	public void setCount(int count)
	{
		_count = count;
	}
}
