package test3.viewmodel;

import javax.validation.constraints.Min;

public class BuyCarModel
{
	private int buyerId;
	private int carId;
	private int payment;
	
	@Min(value=1, message="{error.number.min}")
	public int getBuyerId()
	{
		return buyerId;
	}
	
	public void setBuyerId(int buyerId)
	{
		this.buyerId = buyerId;
	}
	
	@Min(value=1, message="{error.number.min}")
	public int getCarId()
	{
		return carId;
	}
	
	public void setCarId(int carId)
	{
		this.carId = carId;
	}
	
	@Min(value=1, message="{error.number.min}")
	public int getPayment()
	{
		return payment;
	}
	
	public void setPayment(int payment)
	{
		this.payment = payment;
	}
}
