package service.fee;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import exceptions.NullAccountException;
import exceptions.NullListException;
import model.FeeModel;

public class HibernateFee implements Fee
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private SessionFactory _sessionFactory;
	
	public HibernateFee() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Configuration config = new Configuration().configure((String)context.getBean("configXML"));
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
			_sessionFactory = config.buildSessionFactory(serviceRegistry);
		}
	}
	
	@Override
	public void create(FeeModel feeModel) throws Exception
	{
		Session session = _sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(feeModel);
		transaction.commit();
		session.close();
	}

	@Override
	public void update(FeeModel feeModel) throws Exception
	{
		Session session = _sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(feeModel);
		transaction.commit();
		session.close();
	}

	@Override
	public void delete(FeeModel feeModel) throws Exception
	{
		Session session = _sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(feeModel);
		transaction.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FeeModel find(FeeModel feeModel) throws Exception
	{
		List<FeeModel> list;
		Session session = _sessionFactory.openSession();
		Criteria criteria = session.createCriteria(FeeModel.class);
		criteria.add(Restrictions.eq("name", feeModel.getName()));
		list = criteria.list();
		if (list.isEmpty())
			throw new NullAccountException();
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeeModel> list() throws Exception
	{
		List<FeeModel> list;
		Session session = _sessionFactory.openSession();
		Criteria criteria = session.createCriteria(FeeModel.class);
		criteria.addOrder(Order.asc("id"));
		list = criteria.list();
		if (list.isEmpty())
			throw new NullListException();
		return list;
	}
}
