package exceptions;

public class SameLocationException extends WarehouseLogisticsException
{
	public SameLocationException()
	{
		super("error.sameLocation");
	}
}
