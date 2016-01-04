package service.account;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import model.WaybillModel;

public class WaybillDatabase
{
	private final String CONFIG = "service/account/hibernate-config.xml";
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;
	
	public WaybillDatabase() throws Exception
	{
		Configuration config = new Configuration().configure(CONFIG);
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
		ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
		sessionFactory = config.buildSessionFactory(serviceRegistry);
	}
	
	public void create(WaybillModel waybill) throws Exception
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(waybill);
		transaction.commit();
		session.close();
	}
	
	public void update(int id, WaybillModel waybill) throws Exception
	{
		waybill.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(waybill);
		transaction.commit();
		session.close();	
	}
	
	public void delete(int id) throws Exception
	{
		WaybillModel waybill = new WaybillModel();
		waybill.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.delete(waybill);
		transaction.commit();
		session.close();	
	}
	
	public WaybillModel find(int id) throws Exception
	{
		WaybillModel waybill = new WaybillModel();
		session = sessionFactory.openSession();
		waybill = (WaybillModel)session.get(WaybillModel.class, id);
		return waybill;
	}
	
	@SuppressWarnings("unchecked")
	public List<WaybillModel> list() throws Exception
	{
		List<WaybillModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(WaybillModel.class);
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			return null;
		return list;
	}
}
