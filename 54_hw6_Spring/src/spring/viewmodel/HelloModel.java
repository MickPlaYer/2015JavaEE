package spring.viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class HelloModel
{
	private String _name;
	
	@NotEmpty(message = "{error.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF]+", message = "{error.charOnly}")
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
}
