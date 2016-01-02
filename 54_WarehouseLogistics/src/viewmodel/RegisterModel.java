package viewmodel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.AssertTrue;

import org.hibernate.validator.constraints.NotBlank;

import converter.MD5Converter;

public class RegisterModel
{
	@NotBlank(message = "{error.userName.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9_]+.{7,32}", message = "{error.userName.format}")
	private String name;
	@NotBlank(message = "{error.password.empty}")
	@Pattern(regexp = "[a-zA-Z0-9_]+.{7,32}", message = "{error.password.format}")
	private String password;
	private String passwordCheck;
	private boolean passwordChecked;
	
	@AssertTrue(message = "{error.passwordCheckFail}")
	public boolean isPasswordChecked()
	{
		passwordChecked = password.equals(passwordCheck);
		return passwordChecked;
	}
	
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
	
	public String getPasswordCheck()
	{
		return passwordCheck;
	}
	
	public void setPasswordCheck(String passwordCheck)
	{
		this.passwordCheck = passwordCheck;
	}
}
