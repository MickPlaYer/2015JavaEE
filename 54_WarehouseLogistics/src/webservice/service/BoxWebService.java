package webservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import model.AccountModel;
import model.BoxModel;
import model.ItemAmountModel;
import model.ItemBoxModel;
import model.ItemModel;

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

import exceptions.BoxPrivilegeException;
import exceptions.NullBoxException;
import exceptions.NullItemException;
import exceptions.WarehouseLogisticsException;
import service.BoxService;
import service.database.AccountDatabase;
import service.database.BoxDatabase;
import viewmodel.ModifyItemModel;
import webservice.requestmodel.AddItemModel;
import webservice.requestmodel.BoxGetModel;
import webservice.responsemodel.BoxWSModel;
import webservice.responsemodel.ItemBoxWSModel;

@RestController()
@RequestMapping("/webservice/box")
public class BoxWebService
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
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<BoxWSModel> getMyBoxList(@RequestBody BoxGetModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabae.findByToken(model.getToken());
		BoxDatabase boxDatabase = new BoxDatabase(sessionFactory);
		List<BoxModel> list = boxDatabase.listByOwner(account.getId());
		List<BoxWSModel> boxList = new ArrayList<BoxWSModel>();
		for (BoxModel b : list)
		{
			if (b.getHashCode() != null)
				continue;
			BoxWSModel box = new BoxWSModel();
			box.setId(b.getId());
			box.setName(b.getName());
			box.setLocation(b.getLocation());
			boxList.add(box);
		}
		return boxList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}", produces = "application/json")
	public @ResponseBody List<ItemAmountModel> getMyBoxItem(@PathVariable("id") int id, @RequestBody BoxGetModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabae.findByToken(model.getToken());
		BoxDatabase boxDatabase = new BoxDatabase(sessionFactory);
		BoxModel box = boxDatabase.find(id);
		if (account.getId() != box.getOwner())
			throw new BoxPrivilegeException();
		List<ItemAmountModel> list;
		try	{ list = boxDatabase.listItemAmount(box); }
		catch (NullItemException exception)	{ list = new ArrayList<ItemAmountModel>(); }
		return list;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/add", produces = "application/json")
	public @ResponseBody ItemBoxWSModel addItemToBox(@PathVariable("id") int boxId, @RequestBody AddItemModel model) throws Exception
	{
		AccountDatabase accountDatabae = new AccountDatabase(sessionFactory);
		AccountModel account = accountDatabae.findByToken(model.getToken());	
		BoxService boxService = new BoxService(account.getId(), sessionFactory);
		ModifyItemModel modifyItem = new ModifyItemModel();
		modifyItem.setName(model.getItemName());
		modifyItem.setAmount(model.getAmount());
		boxService.addItemToBox(boxId, modifyItem);
		ItemModel item = boxService.findItemByName(model.getItemName());
		ItemBoxModel itemBox = boxService.findItemBox(boxId, item.getId());
		ItemBoxWSModel itemBoxModel = new ItemBoxWSModel();
		itemBoxModel.setItemId(itemBox.getItemId());
		itemBoxModel.setBoxId(itemBox.getBoxId());
		itemBoxModel.setAmount(itemBox.getAmount());
		return itemBoxModel;
	}
	
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
