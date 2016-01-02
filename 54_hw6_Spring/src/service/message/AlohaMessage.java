package service.message;

public class AlohaMessage implements Message {

	@Override
	public String doHello(String name) {
		// The message work.
		return "Aloha, " + name;
	}

}
