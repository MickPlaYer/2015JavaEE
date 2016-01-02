package mailservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.message.Message;
import service.message.HelloMessage;
import service.message.HiMessage;
import service.message.AlohaMessage;

public class HelloServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String result;
		
		// Chose message server.
		Message message = new HelloMessage();
		// Message message = new HiMessage();
		// Message message = new AlohaMessage();
		
		// Get result by message server.
		result = message.doHello(name);
		// Set message server result to request.
		request.setAttribute("result", result);
		// Dispatcher to MailServlet.
		request.getRequestDispatcher("sendMail").forward(request, response);
	}

}
