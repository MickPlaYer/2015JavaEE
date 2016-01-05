package service.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import exceptions.NullBoxException;
import model.BoxModel;
import model.ItemBoxModel;
import model.ItemModel;
import model.ItemAmountModel;

public class BoxDatabase
{
	private final String CONFIG = "service/database/hibernate-config.xml";
	private SessionFactory sessionFactory;
	private Session session;
	private Criteria criteria;
	private Transaction transaction;
	
	public BoxDatabase() throws Exception
	{
		Configuration config = new Configuration().configure(CONFIG);
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
		ServiceRegistry serviceRegistry = ssrb.applySettings(config.getProperties()).build();
		sessionFactory = config.buildSessionFactory(serviceRegistry);
	}
	
	public void create(BoxModel box) throws Exception
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(box);
		transaction.commit();
		session.close();
	}
	
	public void update(int id, BoxModel box) throws Exception
	{
		box.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(box);
		transaction.commit();
		session.close();	
	}
	
	public void delete(int id) throws Exception
	{
		BoxModel box = new BoxModel();
		box.setId(id);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.delete(box);
		transaction.commit();
		session.close();	
	}
	
	public BoxModel find(int id) throws Exception
	{
		BoxModel box = new BoxModel();
		session = sessionFactory.openSession();
		box = (BoxModel)session.get(BoxModel.class, id);
		session.close();
		if (box == null)
			throw new NullBoxException();
		return box;
	}
	
	/*@SuppressWarnings("unchecked")
	public BoxModel findByName(String name) throws Exception
	{
		List<BoxModel> list;
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(BoxModel.class);
		criteria.add(Restrictions.eq("name", name));
		list = criteria.list();
		if (list.isEmpty())
			throw new NullBoxException();
		return list.get(0);
	}*/
	
	@SuppressWarnings("unchecked")
	public List<BoxModel> list() throws Exception
	{
		List<BoxModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(BoxModel.class);
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullBoxException();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<BoxModel> listByOwner(int owner) throws Exception
	{
		List<BoxModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(BoxModel.class);
		criteria.add(Restrictions.eq("owner", owner));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			throw new NullBoxException();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemAmountModel> listItemAmount(BoxModel box) throws Exception
	{
		List<ItemAmountModel> list;
		session = sessionFactory.openSession();
		list = session.createSQLQuery(
				"SELECT Item.id, Item.name, Item_in_box.amount"
				+ " FROM Item, Item_in_box, Box"
				+ " WHERE Item.id = Item_in_box.iid"
				+ " AND Box.id = Item_in_box.bid"
				+ " AND Item_in_box.bid = " + box.getId()
				+ " ORDER BY Item_in_box.iid"
				).addEntity(ItemAmountModel.class).list();
		session.close();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ItemAmountModel findItemAmount(int id, int iid)
	{
		List<ItemAmountModel> list;
		session = sessionFactory.openSession();
		list = session.createSQLQuery(
				"SELECT Item.id, Item.name, Item_in_box.amount"
				+ " FROM Item, Item_in_box, Box"
				+ " WHERE Item.id = Item_in_box.iid"
				+ " AND Box.id = Item_in_box.bid"
				+ " AND Item.id = " + iid
				+ " AND Box.id = " + id).addEntity(ItemAmountModel.class).list();
		session.close();
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public ItemModel findItemByName(String name)
	{
		List<ItemModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(ItemModel.class);
		criteria.add(Restrictions.eq("name", name));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public void createItem(ItemModel item)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(item);
		transaction.commit();
		session.close();
	}

	public void createItemBox(ItemBoxModel itemBox)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.save(itemBox);
		transaction.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public ItemBoxModel findItemBox(int bid, int iid)
	{
		List<ItemBoxModel> list;
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(ItemBoxModel.class);
		criteria.add(Restrictions.eq("boxId", bid));
		criteria.add(Restrictions.eq("itemId", iid));
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public void updateItemBox(ItemBoxModel itemBox)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(itemBox);
		transaction.commit();
		session.close();
	}

	public void deleteItemBox(ItemBoxModel itemBox)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.delete(itemBox);
		transaction.commit();
		session.close();
	}

	public ItemModel findItem(int id)
	{
		ItemModel box = new ItemModel();
		session = sessionFactory.openSession();
		box = (ItemModel)session.get(ItemModel.class, id);
		session.close();
		return box;
	}

	@SuppressWarnings("unchecked")
	public List<ItemModel> listItem()
	{
		List<ItemModel> list;
		session = sessionFactory.openSession();
		criteria = session.createCriteria(ItemModel.class);
		list = criteria.list();
		session.close();
		if (list.isEmpty())
			return null;
		return list;
	}

	public void updateItemBoxList(List<ItemBoxModel> updateList)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		for (ItemBoxModel b : updateList)
		{
			session.update(b);
		}
		transaction.commit();
		session.close();
	}

	public void deleteItemBoxList(List<ItemBoxModel> deleteList)
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		for (ItemBoxModel b : deleteList)
		{
			session.delete(b);
		}
		transaction.commit();
		session.close();
	}
}
