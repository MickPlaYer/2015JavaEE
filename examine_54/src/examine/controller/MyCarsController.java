package examine.controller;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class MyCarsController extends SpringController
{
	@RequestMapping(value = "/myCars", method = RequestMethod.GET)
	public ModelAndView getQueryCarsPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String queryCarsPage = (String)context.getBean("queryCarsPage");
			return new ModelAndView(queryCarsPage);
		}
	}
}
