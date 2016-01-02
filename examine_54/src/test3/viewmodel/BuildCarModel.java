package test3.viewmodel;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class BuildCarModel
{
	private int builderId;
	private String carName;
	private int carPrice;
	
	@Min(value=1, message="{error.number.min}")
	public int getBuilderId()
	{
		return builderId;
	}
	
	public void setBuilderId(int builderId)
	{
		this.builderId = builderId;
	}

	@Length(max = 50, message = "{error.name.tooLong}")
	@NotEmpty(message = "{error.name.empty}")
	@Pattern(regexp = "[a-zA-Z\u4E00-\u9FFF0-9]+", message = "{error.name.charOnly}")
	public String getCarName()
	{
		return carName;
	}

	public void setCarName(String carName)
	{
		this.carName = carName;
	}
	
	@Min(value=1, message="{error.number.min}")
	public int getCarPrice()
	{
		return carPrice;
	}

	public void setCarPrice(int carPrice)
	{
		this.carPrice = carPrice;
	}
}
