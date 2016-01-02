package test3.viewmodel;

import javax.validation.constraints.Min;

public class QueryModel
{
	private int id;

	@Min(value=1, message="{error.number.min}")
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
}
