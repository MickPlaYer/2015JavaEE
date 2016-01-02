package mailservice;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.message.Message;
import service.message.HelloMessage;
import service.message.HiMessage;
import service.mail.GoogleMail;
import service.mail.Mail;
import service.message.AlohaMessage;



public class HelloServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String result;
		String stamp;
		
		// Chose message server.
		Message message = new HelloMessage();
		// Message message = new HiMessage();
		// Message message = new AlohaMessage();
		
		// Chose mail server.
		Mail mail = new GoogleMail();
		// Mail mail = new xxx();
		// ...
		
		// Get result by message server.
		result = message.doHello(name);
		// Get stamp after mail send.
		stamp = mail.sendMail(result);
		
		// Set message server result to request.
		request.setAttribute("result", result);
		// Set mail stamp to request.
		request.setAttribute("stamp", stamp);
		// Push results to message.jsp.
		request.getRequestDispatcher("message.jsp").forward(request, response);
	}
	
}
