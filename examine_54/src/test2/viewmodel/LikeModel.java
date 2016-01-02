package test2.viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class LikeModel
{
	private String _name;

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
}
