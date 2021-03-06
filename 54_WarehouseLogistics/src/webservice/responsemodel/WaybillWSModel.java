package webservice.responsemodel;

public class WaybillWSModel
{
	private int id;
	private String contents;
	private String from;
	private String to;
	private int status;
	private int fee;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getContents()
	{
		return contents;
	}

	public void setContents(String contents)
	{
		this.contents = contents;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getFee()
	{
		return fee;
	}

	public void setFee(int fee)
	{
		this.fee = fee;
	}
}
