package exceptions;

public class PageNotFoundException extends WarehouseLogisticsException
{
	public PageNotFoundException()
	{
		super("error.pageNotFound");
	}

}
