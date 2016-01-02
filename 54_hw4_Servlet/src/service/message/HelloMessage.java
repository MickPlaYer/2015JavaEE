package service.message;

public class HelloMessage implements Message {

	@Override
	public String doHello(String name) {
		// The message work.
		return "Hello, " + name;
	}

}
