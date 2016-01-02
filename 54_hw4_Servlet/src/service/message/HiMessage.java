package service.message;

public class HiMessage implements Message {

	@Override
	public String doHello(String name) {
		// The message work.
		return "Hi, " + name;
	}

}