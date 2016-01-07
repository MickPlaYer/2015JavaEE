package exceptions;

public class ItemNotEnoughException extends WarehouseLogisticsException
{
	public ItemNotEnoughException()
	{
		super("error.itemNotEnough");
	}
}
