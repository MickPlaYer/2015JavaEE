package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloModel
{
	private String _name;
	
	@Length(max = 50, message = "{error.tooLong}")
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
