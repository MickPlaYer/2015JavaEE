package car.mailservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.mail.*;

public class MailServlet extends HttpServlet
{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		String email = request.getParameter("email");
		String detail = (String)request.getAttribute("detail");
		Mail mail = new GoogleMail();
		String stamp = mail.sendMail(detail, email);
		request.setAttribute("stamp", stamp);
		request.getRequestDispatcher("pickcar.jsp").forward(request, response);
	}
	
}
