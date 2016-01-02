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

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import exceptions.NullAccountException;
import exceptions.FeeNotPaidException;
import viewmodel.FeeProviderModel;
import viewmodel.HelloModel;
import model.FeeModel;

@Controller("controller.webservice.QuitController")
@RequestMapping("/webservice")
public class QuitController {
	
	RestTemplate restTemplate = new RestTemplate();
	
	ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/quit", method = RequestMethod.GET)
	public ModelAndView quit() {
		ApplicationContext context = new ClassPathXmlApplicationContext("controller/webservice/spring.xml");
		
		String QUIT = (String) context.getBean("QUIT");
		return new ModelAndView(QUIT);
	}
	
	@RequestMapping(value = "/doQuit", method = RequestMethod.POST)
	public ModelAndView doQuit(@Valid HelloModel helloModel, BindingResult bindingResult) {		
		ApplicationContext context = new ClassPathXmlApplicationContext("controller/webservice/spring.xml");
		String ERROR = (String) context.getBean("ERROR");
		
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		
		if (bindingResult.hasErrors())  {
			return new ModelAndView(ERROR, "ErrorModel", bindingResult.getFieldErrors());
		}
		
		String fee_service =(String) context.getBean("fee_service");
		String fee_provider = fee_service.split("/")[3].split("_")[0];
		
		String name = helloModel.getName();
		
		FeeModel feeModel = (FeeModel) context.getBean("feeModel");
		feeModel.setName(name);	
		
		int count;
		
		try {
			try{
				feeModel = restTemplate.getForObject(new StringBuilder(fee_service).append("/findByName?name=").append(feeModel.getName()).toString(), FeeModel.class);
			}catch (RestClientException restClientException){
				String statusCode = restClientException.getMessage();
				
				if(statusCode.equals("404 Not Found")){
					throw new NullAccountException();
				}else{
					throw restClientException;
				}
			}
			
			count = feeModel.getCount();
			
			if (count > 0) {					
				throw new FeeNotPaidException();							
			} else if (count == 0) {
				restTemplate.delete(new StringBuilder(fee_service).append("/").append(feeModel.getId()).toString());
			}
		} catch (NullAccountException e) {
			feeErrors.add(new FieldError("QuitController", e.getMessage(), resource.getString(e.getMessage())));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		} catch (FeeNotPaidException e) {
			feeErrors.add(new FieldError("QuitController", e.getMessage(), resource.getString(e.getMessage())));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		} catch (RestClientException e) {
			feeErrors.add(new FieldError("QuitController", "error.http", resource.getString("error.http")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		} catch(Exception e){
			feeErrors.add(new FieldError("QuitController", "error.database", resource.getString("error.database")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		}
		
		FeeProviderModel feeProviderModel = (FeeProviderModel) context.getBean("feeProviderModel");
		feeProviderModel.setFeeModel(feeModel);
		feeProviderModel.setFeeProvider(fee_provider);
		
		String SUCCESS = (String) context.getBean("quitSUCCESS");
		return new ModelAndView(SUCCESS, "FeeProviderModel", feeProviderModel);
	}
}
