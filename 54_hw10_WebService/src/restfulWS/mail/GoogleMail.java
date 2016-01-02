package restfulWS.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("Spring.webservice.MailController")
@RequestMapping("/webservice/mail")
public class GoogleMail implements Mail, MailAccount {

	private Session mailSession;

	public GoogleMail()
	{
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

	// http://localhost:8080/00_hw10_WebService/spring/webservice/mail/sendMail
	// http://ilab.csie.ntut.edu.tw:8080/00_hw10_WebService/spring/webservice/mail/sendMail
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String sendMail(@RequestBody String message)
	{
		return sendMail(message, reciver);
	}
	
	public String sendMail(String message, String reciver)
	{
		String stamp = null;
		try
		{
			Message mailMessage = new MimeMessage(mailSession);
			mailMessage.setFrom(new InternetAddress(sender));
			mailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(reciver, false));
			mailMessage.setSubject(subject);
			mailMessage.setText(message);

			Date date = new Date();
			mailMessage.setSentDate(date);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
			stamp = dateFormat.format(date);

			Transport.send(mailMessage);
		}
		catch (Throwable e)
		{
			if (e.toString().contains("SendFailedException"))
				return sendMail(message);
			stamp = e.toString();
		}
		return stamp;
	}
}
