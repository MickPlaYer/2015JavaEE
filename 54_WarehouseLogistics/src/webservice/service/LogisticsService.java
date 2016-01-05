package webservice.service;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import model.AccountModel;
import model.BoxModel;
import model.ItemBoxModel;
import model.ItemModel;
import model.WaybillModel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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
import webservice.requestmodel.BoxGetModel;
import webservice.responsemodel.WaybillWSModel;

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
		int fee = 10;
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = new WaybillModel();
		account = accountDatabae.findByToken(model.getToken());
		
		RestTemplate restTemplate = new RestTemplate();
		CheckPaymentModel checkPaymentModel = new CheckPaymentModel();
		checkPaymentModel.setLogid(model.getPaymentId());
		checkPaymentModel.setValue(fee);
		String uri = "http://ilab.csie.ntut.edu.tw:8080/final_53/spring/bank/checkStatus";
		int success = restTemplate.postForObject(uri, checkPaymentModel, int.class);
		if (success != 1)
			return -1;
		
		String from = "";
		String contents = "";
		List<ItemBoxModel> list = model.getList();
		List<ItemBoxModel> deleteList = new ArrayList<ItemBoxModel>();
		List<ItemBoxModel> updateList = new ArrayList<ItemBoxModel>();
		List<Integer> boxIds = new ArrayList<Integer>();
		List<BoxModel> boxList;
		boxList = boxDatabase.listByOwner(account.getId());
		List<Integer> accountBoxIds = new ArrayList<Integer>();
		for (BoxModel b : boxList)
		{
			accountBoxIds.add(b.getId());
		}
		
		for (ItemBoxModel i : list)
		{
			if (!boxIds.contains(i.getBoxId()))
			{
				boxIds.add(i.getBoxId());
				for (BoxModel b : boxList)
				{
					if (b.getId() == i.getBoxId())
					{
						from += b.getLocation() + ", ";
					}
				}
			}
		}

		for (ItemBoxModel i : list)
		{
			if (!accountBoxIds.contains(i.getBoxId()))
				throw new Exception("Not allow use the box.");
			ItemModel item = boxDatabase.findItem(i.getItemId());
			if (item == null)
				throw new Exception("Can't not find item.");
			ItemBoxModel itemBox = boxDatabase.findItemBox(i.getBoxId(), i.getItemId());
			if (itemBox == null)
				throw new Exception("Can't not find item in box.");
			itemBox.addAmount(-i.getAmount());
			if (itemBox.getAmount() < 0)
				throw new Exception("Item amount not enough.");
			else if (itemBox.getAmount() == 0)
				deleteList.add(itemBox);
			else
				updateList.add(itemBox);
			
			contents += item.getName() + " x " + i.getAmount() + ", ";
		}
		boxDatabase.updateItemBoxList(updateList);
		boxDatabase.deleteItemBoxList(deleteList);
		waybill.setAccountId(account.getId());
		waybill.setContents(contents);
		waybill.setFrom(from);
		waybill.setTo(model.getLocation());
		waybill.setFee(fee);
		waybill.setStatus(2);
		waybill = waybillDatabase.create(waybill);
		return waybill.getId();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody WaybillWSModel getWaybill(@PathVariable("id") int id, @RequestBody BoxGetModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase();
		AccountModel account = accountDatabae.findByToken(model.getToken());
		WaybillDatabase waybillDatabase = new WaybillDatabase();
		WaybillModel waybill = waybillDatabase.find(id);
		if (waybill == null)
			throw new NullAccountException();
		if (waybill.getAccountId() != account.getId())
			throw new Exception("Can't view the waybill.");
		WaybillWSModel waybillWS = new WaybillWSModel();
		waybillWS.setContents(waybill.getContents());
		waybillWS.setFee(waybill.getFee());
		waybillWS.setFrom(waybill.getFrom());
		waybillWS.setId(waybill.getId());
		waybillWS.setStatus(waybill.getStatus());
		waybillWS.setTo(waybill.getTo());
		return waybillWS;
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
