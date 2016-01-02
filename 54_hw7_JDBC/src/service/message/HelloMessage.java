package service.message;

public class HelloMessage implements Message
{
	@Override
	public String doMessage(String name)
	{
		return "Hello, " + name;
	}
}
