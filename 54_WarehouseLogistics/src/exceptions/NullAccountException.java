package exceptions;

public class NullAccountException extends WarehouseLogisticsException
{
	public NullAccountException()
	{
		super("error.nullAccount");
	}
}
