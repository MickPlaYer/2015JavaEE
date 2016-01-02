package model;

import java.util.Date;

public class BoxModel
{
	private int id;
	private int owner;
	private String name;
	private String location;
	private Date deadline;
	private String hashCode;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public int getOwner()
	{
		return owner;
	}

	public void setOwner(int owner)
	{
		this.owner = owner;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Date getDeadline()
	{
		return deadline;
	}

	public void setDeadline(Date deadline)
	{
		this.deadline = deadline;
	}

	public String getHashCode()
	{
		return hashCode;
	}

	public void setHashCode(String hashCode)
	{
		this.hashCode = hashCode;
	}
}
