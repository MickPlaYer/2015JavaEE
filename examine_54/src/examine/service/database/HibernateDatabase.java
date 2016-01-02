package examine.service.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import examine.exceptios.NullCarException;
import examine.exceptios.NullCarListException;
import examine.exceptios.NullOwnerException;
import examine.exceptios.NullOwnerListException;
import examine.model.CarModel;
import examine.model.OwnerModel;

public class HibernateDatabase implements Database
{
	private final String CONTEXT_FILE = "examine/controller/spring.xml";
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;

	public HibernateDatabase() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			Configuration config = new Configuration().configure((String)context.getBean("HibernateConfig"));
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
		}
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<CarModel> getCarlist() throws Exception
	{
		List<CarModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(CarModel.class);
		criteria.addOrder(Order.asc("id"));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullCarListException();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OwnerModel> getOwnerList() throws Exception
	{
		List<OwnerModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(OwnerModel.class);
		criteria.addOrder(Order.asc("id"));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullOwnerListException();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OwnerModel findOwner(int id) throws Exception
	{
		List<OwnerModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(OwnerModel.class);
		criteria.add(Restrictions.eq("id", id));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullOwnerException();
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CarModel findCar(int id) throws Exception
	{
		List<CarModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(CarModel.class);
		criteria.add(Restrictions.eq("id", id));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullCarException();
		return list.get(0);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<CarModel> getOwnerCars(int id) throws Exception
	{
		List<CarModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(CarModel.class);
		criteria.add(Restrictions.eq("ownerId", id));
		criteria.addOrder(Order.asc("id"));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullCarListException();
		return list;
	}

	@Override
	public void buyCar(OwnerModel buyer, OwnerModel seller, CarModel car, int payment) throws Exception
	{
		buyer.addCash(-payment);
		buyer.addAsset(payment);
		buyer.addCount(1);
		seller.addCash(payment);
		seller.addAsset(-payment);
		seller.addCount(-1);
		car.setOwnerId(buyer.getId());
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.update(buyer);
			session.update(seller);
			session.update(car);
			transaction.commit();
		}
		catch (HibernateException e)
		{
			if(transaction != null)
			{
				transaction.rollback();
			}
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}*/
}
