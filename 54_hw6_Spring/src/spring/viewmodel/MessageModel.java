package spring.viewmodel;

public class MessageModel
{
	String _result;
	String _stamp;
	
	public void setResults(String result, String stamp)
	{
		_result = result;
		_stamp = stamp;
	}

	public String getResult()
	{
		return _result;
	}
	
	public String getStamp()
	{
		return _stamp;
	}
}
