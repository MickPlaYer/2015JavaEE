package car.simpleservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import carrace.cars.Buick;

public class BuickServlet extends HttpServlet
{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		Buick buick = new Buick();
		request.setAttribute("name", buick.getName());
		request.getRequestDispatcher("pickcar.jsp").forward(request, response);
	}
	
}
