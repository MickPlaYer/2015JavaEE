package exceptions;

public class AccountExistException extends WarehouseLogisticsException
{
	public AccountExistException()
	{
		super("error.accountExist");
	}
}
