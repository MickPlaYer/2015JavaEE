package exceptions;

public class AccountExistException extends Exception
{
	public AccountExistException()
	{
		super("error.accountExist");
	}
}
