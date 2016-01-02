package simpleservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlohaServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String result = "Aloha, " + name;
		// Set result to request.
		request.setAttribute("result", result);
		// Push result to message.jsp.
		request.getRequestDispatcher("message.jsp").forward(request, response);
	}

}
