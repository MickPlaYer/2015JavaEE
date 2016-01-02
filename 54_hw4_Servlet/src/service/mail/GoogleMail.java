package service.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GoogleMail implements Mail, MailAccount {

	private Session mailSession;

	public GoogleMail() {

		Properties mailProperties = System.getProperties();
		mailProperties.put("mail.smtp.host", host);
		mailProperties.put("mail.smtp.auth", "true");
		mailProperties.put("mail.smtp.socketFactory.port", port);
		mailProperties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		mailProperties.put("mail.smtp.socketFactory.fallback", "false");

		mailSession = Session.getInstance(mailProperties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(account, password);
					}
				});
		mailSession.setDebug(false);
	}

	public String sendMail(String message) {
		return sendMail(message, reciver);
	}
	
	public String sendMail(String message, String reciver) {

		String stamp = null;
		try {
			Message mailMessage = new MimeMessage(mailSession);
			mailMessage.setFrom(new InternetAddress(sender));
			mailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(reciver, false));
			mailMessage.setSubject(subject);
			mailMessage.setText(message);

			Date date = new Date();
			mailMessage.setSentDate(date);

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd kk:mm:ss");
			stamp = dateFormat.format(date);

			Transport.send(mailMessage);

		} catch (Throwable e) {
			if (e.toString().contains("SendFailedException"))
				return sendMail(message);
			stamp = e.toString();
		}

		return stamp;
	}
}
