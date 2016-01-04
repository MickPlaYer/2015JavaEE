package service.account;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import exceptions.NullAccountException;
import model.AccountModel;

public class AccountDatabase
{
	private final String CONFIG = "service/account/hibernate-config.xml";
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;
	
	public AccountDatabase() throws Exception
	{
		Configuration config = new Configuration().configure(CONFIG);
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
		ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
		sessionFactory = config.buildSessionFactory(serviceRegistry);
	}
	
	public void create(AccountModel account) throws Exception
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(account);
		transaction.commit();
		session.close();
	}
	
	public void update(int id, AccountModel account) throws Exception
	{
		account.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(account);
		transaction.commit();
		session.close();	
	}
	
	public void delete(int id) throws Exception
	{
		AccountModel account = new AccountModel();
		account.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.delete(account);
		transaction.commit();
		session.close();	
	}
	
	public AccountModel find(int id) throws Exception
	{
		AccountModel account = new AccountModel();
		session = sessionFactory.openSession();
		account = (AccountModel)session.get(AccountModel.class, id);
		if (account == null)
			throw new NullAccountException();
		return account;
	}
	
	@SuppressWarnings("unchecked")
	public AccountModel findByName(String name) throws Exception
	{
		List<AccountModel> list;
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(AccountModel.class);
		criteria.add(Restrictions.eq("name", name));
		list = criteria.list();
		if (list.isEmpty())
			throw new NullAccountException();
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountModel> list() throws Exception
	{
		List<AccountModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(AccountModel.class);
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullAccountException();
		return list;
	}

	@SuppressWarnings("unchecked")
	public AccountModel findByToken(String token) throws NullAccountException
	{
		List<AccountModel> list;
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(AccountModel.class);
		criteria.add(Restrictions.eq("token", token));
		list = criteria.list();
		if (list.isEmpty())
			throw new NullAccountException();
		return list.get(0);
	}
}
