package exceptions;

public class BoxUnactivatedException extends WarehouseLogisticsException
{
	public BoxUnactivatedException()
	{
		super("error.boxUnactivated");
	}
}
