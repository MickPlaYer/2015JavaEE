package viewmodel;

public class MessageModel
{
	private String _result;
	private String _stamp;
	private int _count;
	
	public void setResults(String result, String stamp, int count)
	{
		_result = result;
		_stamp = stamp;
		_count = count;
	}

	public String getResult()
	{
		return _result;
	}
	
	public String getStamp()
	{
		return _stamp;
	}
	
	public int getCount()
	{
		return _count;
	}
}
