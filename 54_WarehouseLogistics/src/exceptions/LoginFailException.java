package exceptions;

public class LoginFailException extends Exception
{
	public LoginFailException()
	{
		super("error.loginFail");
	}
}
