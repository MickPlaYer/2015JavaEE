package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import converter.MD5Converter;

public class LoginModel
{
	@NotBlank(message = "{error.userName.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9_]+.{7,32}", message = "{error.userName.format}")
	private String name;
	@NotBlank(message = "{error.password.empty}")
	@Pattern(regexp = "[a-zA-Z0-9_]+.{7,32}", message = "{error.password.format}")
	private String password;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPassword() throws Exception
	{
		return MD5Converter.convert(password);
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
}
