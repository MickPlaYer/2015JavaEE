package spring.controller;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import service.mail.Mail;
import service.message.Message;

import spring.viewmodel.HelloModel;
import spring.viewmodel.MessageModel;

@Controller("Spring.HelloController")
public class HelloController
{
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView getHelloPage()
	{
		String HELLO_PAGE;
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/controller/spring.xml"))
		{
			HELLO_PAGE = (String)context.getBean("HELLO_PAGE");
		}
		return new ModelAndView(HELLO_PAGE);
	}
	
	@RequestMapping(value = "/doHello", method = RequestMethod.POST)
	public ModelAndView doHello(@Valid HelloModel helloModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/controller/spring.xml"))
		{
			String page;
			if (bindingResult.hasErrors())
			{
				page = (String)context.getBean("ERROR_PAGE");
				return new ModelAndView(page, "ErrorModel", bindingResult.getFieldErrors());
			}
			MessageModel messageModel = (MessageModel)context.getBean("messageModel");
			Message message = (Message)context.getBean("message");
			Mail mail = (Mail)context.getBean("googleMail");
			String result = message.doHello(helloModel.getName());
			String stamp = mail.sendMail(result);
			messageModel.setResults(result, stamp);
			page = (String)context.getBean("SUCCESS_PAGE");
			return new ModelAndView(page, "MessageModel", messageModel);
		}
	}
}
