package exceptions;

public class NullWaybillException extends WarehouseLogisticsException
{
	public NullWaybillException()
	{
		super("error.nullWaybill");
	}
}
