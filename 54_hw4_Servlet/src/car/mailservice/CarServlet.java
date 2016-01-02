package car.mailservice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import carrace.race.Race;
import service.mail.*;

public class CarServlet extends HttpServlet
{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		String email = request.getParameter("email");
		String carPick = request.getParameter("car_pick");
		String detail;
		String report;
		String stamp;
		
		Race race = new Race(carPick);
		race.Start();
		detail = race.getDetail();
		report = race.getReport();
		
		Mail mail = new GoogleMail();
		stamp = mail.sendMail(detail, email);

		request.setAttribute("report", report);
		request.setAttribute("stamp", stamp);
		request.getRequestDispatcher("carrace.jsp").forward(request, response);
	}
	
}
