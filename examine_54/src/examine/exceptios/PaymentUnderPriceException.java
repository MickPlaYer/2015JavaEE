package examine.exceptios;

public class PaymentUnderPriceException extends Exception
{
	public PaymentUnderPriceException()
	{
		super("error.paymentUnderPrice");
	}
}
