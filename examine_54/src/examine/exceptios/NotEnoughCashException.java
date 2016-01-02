package examine.exceptios;

public class NotEnoughCashException extends Exception
{
	public NotEnoughCashException()
	{
		super("error.notEnoughCash");
	}
}
