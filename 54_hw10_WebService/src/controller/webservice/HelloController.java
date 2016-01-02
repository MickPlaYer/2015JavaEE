package controller.webservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.validation.Valid;

import viewmodel.MessageModel;
import viewmodel.HelloModel;
import viewmodel.MessageProviderModel;
import model.FeeModel;

@Controller("controller.webservice.HelloController")
@RequestMapping("/webservice")
public class HelloController {
	
	RestTemplate restTemplate = new RestTemplate();
	
	ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	String result;
	String stamp;
	int count;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView hello() {
		ApplicationContext context = new ClassPathXmlApplicationContext("controller/webservice/spring.xml");
		
		String HELLO = (String) context.getBean("HELLO");
		return new ModelAndView(HELLO);
	}
	
	@RequestMapping(value = "/doHello", method = RequestMethod.POST)
	public ModelAndView doHello(@Valid HelloModel helloModel, BindingResult bindingResult){		
		ApplicationContext context = new ClassPathXmlApplicationContext("controller/webservice/spring.xml");
		String ERROR = (String) context.getBean("ERROR");
		
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		
		if (bindingResult.hasErrors())  {
			return new ModelAndView(ERROR, "ErrorModel", bindingResult.getFieldErrors());
		}
		
		String message_service = (String) context.getBean("message_service");
		String mail_service = (String) context.getBean("mail_service");
		String fee_service =(String) context.getBean("fee_service");
		
		String message_provider = message_service.split("/")[3].split("_")[0];
		String mail_provider = mail_service.split("/")[3].split("_")[0];
		String fee_provider = fee_service.split("/")[3].split("_")[0];
		
		String name = helloModel.getName();
		
		FeeModel feeModel = (FeeModel) context.getBean("feeModel");
		feeModel.setName(name);
		
		try {
			result = restTemplate.postForObject(message_service, name, String.class);
			stamp = restTemplate.postForObject(mail_service, result, String.class);
			
			try{
				feeModel = restTemplate.getForObject(new StringBuilder(fee_service).append("/findByName?name=").append(feeModel.getName()).toString(), FeeModel.class);
				
				count = feeModel.getCount();
				
				if (count >=0) {
					feeModel.setCount(++count);
					
					restTemplate.put(new StringBuilder(fee_service).append("/").append(feeModel.getId()).toString(), feeModel);
				}
			}catch (RestClientException restClientException){	
				String statusCode = restClientException.getMessage();
				
				if(statusCode.equals("404 Not Found")){
					feeModel = new FeeModel();
					count = 1;
					
					feeModel.setName(name);
					feeModel.setCount(count);
					restTemplate.postForObject(fee_service, feeModel, FeeModel.class);
				}else{
					throw restClientException;
				}
			}
		}catch (RestClientException e) {
			feeErrors.add(new FieldError("FeeController", "error.http", resource.getString("error.http")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		}catch(Exception e){
			feeErrors.add(new FieldError("FeeController", "error.database", resource.getString("error.database")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		}

		MessageModel messageModel = (MessageModel) context.getBean("messageModel");
		messageModel.setResult(result);
		messageModel.setStamp(stamp);
		messageModel.setCount(count);
		
		MessageProviderModel messageProviderModel = (MessageProviderModel) context.getBean("messageProviderModel");
		messageProviderModel.setMessageModel(messageModel);
		messageProviderModel.setMessageProvider(message_provider);
		messageProviderModel.setMailProvider(mail_provider);
		messageProviderModel.setFeeProvider(fee_provider);
		
		String SUCCESS = (String) context.getBean("helloSUCCESS");
		return new ModelAndView(SUCCESS, "MessageProviderModel", messageProviderModel);
	}
}
