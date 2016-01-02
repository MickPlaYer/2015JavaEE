package car.mailservlet;

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
		CarGarage carGarage = new CarGarage();
		String carPick = request.getParameter("car_pick");
		String detail = carGarage.getCarDetail(carPick);
		Car car = carGarage.pickCar(carPick);
		request.setAttribute("detail", detail);
		request.setAttribute("name", car.getName());
		request.getRequestDispatcher("sendMail").forward(request, response);
	}
	
}
