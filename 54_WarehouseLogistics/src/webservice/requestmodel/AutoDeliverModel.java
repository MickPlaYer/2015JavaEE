package webservice.requestmodel;

import java.util.List;

import model.ItemBoxModel;

public class AutoDeliverModel
{
	private String token;
	private String location;
	private int paymentId;
	private List<ItemBoxModel> list;
	
	public String getToken()
	{
		return token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public int getPaymentId()
	{
		return paymentId;
	}

	public void setPaymentId(int paymentId)
	{
		this.paymentId = paymentId;
	}

	public List<ItemBoxModel> getList()
	{
		return list;
	}

	public void setList(List<ItemBoxModel> list)
	{
		this.list = list;
	}
}
