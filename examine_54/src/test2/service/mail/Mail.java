package test2.service.mail;

public interface Mail {
	String sendMail(String message);
	String sendMail(String message, String reciver);
}
