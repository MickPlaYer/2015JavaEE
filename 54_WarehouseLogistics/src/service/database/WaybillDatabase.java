package service.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import exceptions.NullWaybillException;
import model.WaybillModel;

public class WaybillDatabase
{
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;
	
	public WaybillDatabase(SessionFactory sessionFactory) throws Exception
	{
		this.sessionFactory = sessionFactory;
	}
	
	public WaybillModel create(WaybillModel waybill) throws Exception
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(waybill);
		transaction.commit();
		session.close();
		return waybill;
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
	
	public WaybillModel find(int id) throws NullWaybillException
	{
		WaybillModel waybill = new WaybillModel();
		session = sessionFactory.openSession();
		waybill = (WaybillModel)session.get(WaybillModel.class, id);
		session.close();
		if (waybill == null)
			throw new NullWaybillException();
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

	@SuppressWarnings("unchecked")
	public List<WaybillModel> listByAccount(int account)
	{
		List<WaybillModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(WaybillModel.class);
		criteria.add(Restrictions.eq("accountId", account));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			return null;
		return list;
	}
}
