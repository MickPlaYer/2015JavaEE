package exceptions;

public class NullItemException extends WarehouseLogisticsException
{
	public NullItemException()
	{
		super("error.nullItem");
	}
}
