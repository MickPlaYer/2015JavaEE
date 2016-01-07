package exceptions;

public class SessionFailException extends WarehouseLogisticsException
{
	public SessionFailException()
	{
		super("error.sessionFail");
	}
}
