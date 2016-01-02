package service.message;

public class AlohaMessage implements Message
{
	@Override
	public String doMessage(String name)
	{
		return "Aloha, " + name;
	}
}
