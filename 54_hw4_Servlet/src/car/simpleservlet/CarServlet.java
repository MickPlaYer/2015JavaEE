package car.simpleservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import carrace.cars.*;

public class CarServlet extends HttpServlet
{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		String carPick = request.getParameter("car_pick");
		CarGarage carGarage = new CarGarage();
		Car car = carGarage.pickCar(carPick);
		request.getRequestDispatcher("do" + car.getName()).forward(request, response);
	}
	
}
