package test3.exceptions;

public class PaymentUnderPriceException extends Exception
{
	public PaymentUnderPriceException()
	{
		super("error.paymentUnderPrice");
	}
}
