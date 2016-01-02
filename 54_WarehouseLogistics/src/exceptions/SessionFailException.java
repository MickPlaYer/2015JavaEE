package exceptions;

public class SessionFailException extends Exception
{
	public SessionFailException()
	{
		super("error.sessionFail");
	}
}
