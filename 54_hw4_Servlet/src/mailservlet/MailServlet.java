package mailservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.mail.GoogleMail;
import service.mail.Mail;

public class MailServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String result = (String)request.getAttribute("result");
		String stamp;
		
		// Chose mail server.
		Mail mail = new GoogleMail();
		// Mail mail = new xxx();
		// ...
		
		// Get stamp after mail send.
		stamp = mail.sendMail(result);
		// Set mail stamp to request.
		request.setAttribute("stamp", stamp);
		// Push results to message.jsp.
		request.getRequestDispatcher("message.jsp").forward(request, response);
	}

}
