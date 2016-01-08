package webservice.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import model.AccountModel;
import model.BoxModel;
import model.ItemBoxModel;
import model.ItemModel;
import model.WaybillModel;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import exceptions.BoxPeriodException;
import exceptions.BoxPrivilegeException;
import exceptions.ItemNotEnoughException;
import exceptions.NullBoxException;
import exceptions.NullWaybillException;
import exceptions.WarehouseLogisticsException;
import exceptions.WaybillPrivilegeException;
import service.database.AccountDatabase;
import service.database.BoxDatabase;
import service.database.WaybillDatabase;
import webservice.requestmodel.CheckPaymentModel;
import webservice.requestmodel.AutoDeliverModel;
import webservice.requestmodel.BoxGetModel;
import webservice.responsemodel.WaybillWSModel;

@RestController()
@RequestMapping("/webservice/logistics")
public class LogisticsWebService
{
	protected SessionFactory sessionFactory;
	protected final String CONTEXT_FILE = "spring/controller/spring.xml";
	protected ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE);
	@Autowired
	private HttpSession httpSession;
	
	@ModelAttribute("BeforeDo")
	protected void setupHibernateConfig()
	{
		sessionFactory = (SessionFactory)httpSession.getAttribute("SessionFactory");
		if (sessionFactory == null)
		{
			String CONFIG = (String)context.getBean("hibernateConfig");
			Configuration config = new Configuration().configure(CONFIG);
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
			httpSession.setAttribute("SessionFactory", sessionFactory);
		}
	}
	
	@RequestMapping(value = "/auto", method = RequestMethod.POST)
	public int autoDeliver(@RequestBody AutoDeliverModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabae.findByToken(model.getToken());
		BoxDatabase boxDatabase = new BoxDatabase(sessionFactory);
		int fee = 10;

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
		
		List<BoxModel> accountBoxList = boxDatabase.listByOwner(account.getId());
		List<Integer> accountBoxIds = new ArrayList<Integer>();
		for (BoxModel b : accountBoxList)
		{
			accountBoxIds.add(b.getId());
		}
		
		// Write down "send from location".
		List<Integer> boxIds = new ArrayList<Integer>();
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		for (ItemBoxModel i : list)
		{
			if (!boxIds.contains(i.getBoxId()))
			{
				boxIds.add(i.getBoxId());
				for (BoxModel b : accountBoxList)
				{
					if (b.getId() == i.getBoxId())
					{
						if (today.after(b.getDeadline()))
							throw new BoxPeriodException();
						from += b.getLocation() + ", ";
					}
				}
			}
		}

		// Move items and write down contents.
		List<ItemBoxModel> deleteList = new ArrayList<ItemBoxModel>();
		List<ItemBoxModel> updateList = new ArrayList<ItemBoxModel>();
		for (ItemBoxModel i : list)
		{
			if (!accountBoxIds.contains(i.getBoxId()))
				throw new BoxPrivilegeException();
			ItemModel item = boxDatabase.findItem(i.getItemId());
			ItemBoxModel itemBox = boxDatabase.findItemBox(i.getBoxId(), i.getItemId());
			itemBox.addAmount(-i.getAmount());
			if (itemBox.getAmount() < 0)
				throw new ItemNotEnoughException();
			else if (itemBox.getAmount() == 0)
				deleteList.add(itemBox);
			else
				updateList.add(itemBox);
			contents += item.getName() + " x " + i.getAmount() + ", ";
		}
		boxDatabase.updateItemBoxList(updateList);
		boxDatabase.deleteItemBoxList(deleteList);
		
		if (from.length() >= 300)
		{
			from = from.substring(0, 299);
			from += "...";
		}
		if (contents.length() >= 300)
		{
			contents = contents.substring(0, 299);
			contents += "...";
		}
		
		WaybillDatabase waybillDatabase = new WaybillDatabase(sessionFactory);
		WaybillModel waybill = new WaybillModel();
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
		AccountDatabase accountDatabae = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabae.findByToken(model.getToken());
		WaybillDatabase waybillDatabase = new WaybillDatabase(sessionFactory);
		WaybillModel waybill = waybillDatabase.find(id);
		if (waybill.getAccountId() != account.getId())
			throw new WaybillPrivilegeException();
		WaybillWSModel waybillWS = new WaybillWSModel();
		waybillWS.setContents(waybill.getContents());
		waybillWS.setFee(waybill.getFee());
		waybillWS.setFrom(waybill.getFrom());
		waybillWS.setId(waybill.getId());
		waybillWS.setStatus(waybill.getStatus());
		waybillWS.setTo(waybill.getTo());
		return waybillWS;
	}
	
	@ExceptionHandler(NullWaybillException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullWaybillExceptionException(NullWaybillException e) {}
	
	@ExceptionHandler(NullBoxException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNullBoxExceptionException(NullBoxException e) {}
	
	@ExceptionHandler(WarehouseLogisticsException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public void handleWarehouseLogisticsExceptionException(WarehouseLogisticsException e) {}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception e) { e.printStackTrace(); }
}
