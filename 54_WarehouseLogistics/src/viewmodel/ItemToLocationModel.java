package viewmodel;

import javax.validation.constraints.Pattern;

public class ItemToLocationModel
{
	@Pattern(regexp = ".+.{1,50}", message = "{error.userName.format}")
	private String contents;
	private String location;
	
	public String getContents()
	{
		return contents;
	}
	
	public void setContents(String contents)
	{
		this.contents = contents;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
}
