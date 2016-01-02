package test2.exceptions;

public class NullUserException extends Exception
{
	public NullUserException()
	{
		super("error.nullUser");
	}
}