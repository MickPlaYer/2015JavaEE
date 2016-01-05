package viewmodel;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ItemToLocationModel
{
	@Pattern(regexp = ".+.{1,50}", message = "���e���L��")
	@NotBlank(message = "�ж�J���e��")
	private String contents;
	@NotBlank(message = "�ж�J�ت��a")
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
