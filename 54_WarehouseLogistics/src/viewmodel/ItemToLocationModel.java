package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ItemToLocationModel
{
	@Pattern(regexp = ".+.{1,50}", message = "內容物過長")
	@NotBlank(message = "請填入內容物")
	private String contents;
	@NotBlank(message = "請填入目的地")
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
