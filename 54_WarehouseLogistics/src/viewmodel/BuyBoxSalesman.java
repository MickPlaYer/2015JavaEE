package viewmodel;

import model.BoxModel;

import org.hibernate.validator.constraints.NotBlank;

import webservice.requestmodel.PayModel;

public class BuyBoxSalesman
{
	@NotBlank(message = "{error.boxName.empty}")
	private String boxName;
	@NotBlank(message = "{error.boxLocation.empty}")
	private String boxLocation;
	private String boxPeriod;
	private BoxModel box;
	private PayModel pay;
	
	public String getBoxName()
	{
		return boxName;
	}
	
	public void setBoxName(String boxName)
	{
		this.boxName = boxName;
	}

	public String getBoxLocation()
	{
		return boxLocation;
	}

	public void setBoxLocation(String boxLocation)
	{
		this.boxLocation = boxLocation;
	}

	public String getBoxPeriod()
	{
		return boxPeriod;
	}

	public void setBoxPeriod(String boxPeriod)
	{
		this.boxPeriod = boxPeriod;
	}

	public BoxModel getBox()
	{
		return box;
	}

	public void setBox(BoxModel box)
	{
		this.box = box;
	}

	public PayModel getPay()
	{
		return pay;
	}

	public void setPay(PayModel pay)
	{
		this.pay = pay;
	}
}
