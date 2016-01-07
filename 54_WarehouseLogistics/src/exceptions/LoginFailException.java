package exceptions;

public class LoginFailException extends WarehouseLogisticsException
{
	public LoginFailException()
	{
		super("error.loginFail");
	}
}
