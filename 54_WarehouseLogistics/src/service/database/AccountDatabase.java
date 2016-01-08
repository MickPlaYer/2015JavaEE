package service.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import exceptions.NullAccountException;
import model.AccountModel;

public class AccountDatabase
{
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;
	
	public AccountDatabase(SessionFactory sessionFactory) throws Exception
	{
		this.sessionFactory = sessionFactory;
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
		session.close();
		if (account == null)
			throw new NullAccountException();
		return account;
	}
	
	@SuppressWarnings("unchecked")
	public AccountModel findByName(String name) throws Exception
	{
		List<AccountModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(AccountModel.class);
		criteria.add(Restrictions.eq("name", name));
		list = criteria.list();
		session.close();
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
		session = sessionFactory.openSession();
		criteria = session.createCriteria(AccountModel.class);
		criteria.add(Restrictions.eq("token", token));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullAccountException();
		return list.get(0);
	}
}
