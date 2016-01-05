package webservice.service;

import java.util.List;

import model.AccountModel;
import model.ItemBoxModel;
import model.WaybillModel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import exceptions.NullAccountException;
import exceptions.NullBoxException;
import service.database.AccountDatabase;
import service.database.BoxDatabase;
import service.database.WaybillDatabase;
import viewmodel.CheckPaymentModel;
import webservice.requestmodel.AutoDeliverModel;

@RestController()
@RequestMapping("/webservice/logistics")
public class LogisticsService
{
	@RequestMapping(value = "/auto", method = RequestMethod.POST)
	public int getMyBoxList(@RequestBody AutoDeliverModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase();
		AccountModel account;
		BoxDatabase boxDatabase = new BoxDatabase();
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		account = accountDatabae.findByToken(model.getToken());
		RestTemplate restTemplate = new RestTemplate();
		CheckPaymentModel checkPaymentModel = new CheckPaymentModel();
		checkPaymentModel.setLogid(model.getPaymentId());
		checkPaymentModel.setValue(10);
		String uri = "http://ilab.csie.ntut.edu.tw:8080/final_53/spring/bank/checkStatus";
		int success = restTemplate.postForObject(uri, checkPaymentModel, int.class);
		if (success != 1)
			return -1;
		List<ItemBoxModel> list = model.getList();
		for (ItemBoxModel i : list)
		{
			ItemBoxModel itemBox = boxDatabase.findItemBox(i.getBoxId(), i.getItemId());
			if (itemBox == null)
				throw new Exception("Can't not find item in box.");
			itemBox.addAmount(-i.getAmount());
			if (itemBox.getAmount() < 0)
				throw new Exception("Item amount not enough.");
		}
		return 0;
	}
	
	@ExceptionHandler(NullAccountException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullAccountException(NullAccountException e) {}
	
	@ExceptionHandler(NullBoxException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullBoxExceptionException(NullBoxException e) {}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception e) { e.printStackTrace(); }
}
