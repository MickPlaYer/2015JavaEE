package viewmodel;

import model.BoxModel;

import webservice.requestmodel.PayModel;

public class BoxRenewer
{
	private int boxId;
	private String boxPeriod;
	private BoxModel box;
	private PayModel pay;

	public int getBoxId()
	{
		return boxId;
	}

	public void setBoxId(int boxId)
	{
		this.boxId = boxId;
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
