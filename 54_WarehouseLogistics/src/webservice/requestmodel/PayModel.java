package webservice.requestmodel;

public class PayModel
{
	private String token;
	private int value;
	private String callbackurl;
	
	public String getToken()
	{
		return token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public String getCallbackurl()
	{
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl)
	{
		this.callbackurl = callbackurl;
	}
}
