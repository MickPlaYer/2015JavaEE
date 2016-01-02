package examine.controller;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class SellCarController extends SpringController
{
	@RequestMapping(value = "/sellCar", method = RequestMethod.GET)
	public ModelAndView getSellCarPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String sellCarPage = (String)context.getBean("sellCarPage");
			return new ModelAndView(sellCarPage);
		}
	}
}
