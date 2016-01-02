package car.simpleservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import carrace.cars.Porsche;

public class PorscheServlet extends HttpServlet
{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		Porsche porsche = new Porsche();
		request.setAttribute("name", porsche.getName());
		request.getRequestDispatcher("pickcar.jsp").forward(request, response);
	}
	
}
