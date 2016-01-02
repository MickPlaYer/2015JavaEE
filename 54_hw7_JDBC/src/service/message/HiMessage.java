package service.message;

public class HiMessage implements Message
{
	@Override
	public String doMessage(String name)
	{
		return "Hi, " + name;
	}
}