package controller.webservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.FieldError;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import viewmodel.FeeListProviderModel;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import exceptions.NullListException;
import model.FeeModel;

@Controller("controller.webservice.ListController")
@RequestMapping("/webservice")
public class ListController {
	
	RestTemplate restTemplate = new RestTemplate();
	
	ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/doList", method = RequestMethod.GET)
	public ModelAndView doList() {
		ApplicationContext context = new ClassPathXmlApplicationContext("controller/webservice/spring.xml");
		String ERROR = (String) context.getBean("ERROR");
		
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		
		String fee_service =(String) context.getBean("fee_service");
		String fee_provider = fee_service.split("/")[3].split("_")[0];
		
		List<FeeModel> feeListModel;
		
		try {
			feeListModel = restTemplate.getForObject(fee_service, List.class);
			
			if (feeListModel.size() == 0) {					
				throw new NullListException();
			}
		} catch (NullListException e) {
			feeErrors.add(new FieldError("ListController", e.getMessage(), resource.getString(e.getMessage())));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		} catch (RestClientException e) {
			feeErrors.add(new FieldError("ListController", "error.http", resource.getString("error.http")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		} catch(Exception e){
			feeErrors.add(new FieldError("ListController", "error.database", resource.getString("error.database")+"<br>"+e.getMessage()));
			return new ModelAndView(ERROR, "ErrorModel", feeErrors);
		}
		
		FeeListProviderModel feeListProviderModel = (FeeListProviderModel) context.getBean("feeListProviderModel");
		feeListProviderModel.setFeeListModel(feeListModel);
		feeListProviderModel.setFeeProvider(fee_provider);
		
		String LIST = (String) context.getBean("listSUCCESS");
		return new ModelAndView(LIST, "FeeListProviderModel", feeListProviderModel);
	}
}
