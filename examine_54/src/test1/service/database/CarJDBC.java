package test1.service.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import test1.exceptions.NullCarException;
import test1.exceptions.NullListException;
import test1.model.DatabaseModel;

public class CarJDBC implements CarDatabase
{
	private final String CONTEXT_FILE = "test1/controller/spring.xml";
	private DataSource _feeDataSource;

	public CarJDBC() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			InitialContext contextJNDI = new InitialContext();
			_feeDataSource = (DataSource)contextJNDI.lookup((String)context.getBean("DataSource"));
		}
		catch (Exception exception) { throw exception; }
	}
	
	@Override
	public DatabaseModel findByName(String carName) throws Exception
	{
		String sqlCommand = "SELECT * FROM Car WHERE NAME=?";
		DatabaseModel model = new DatabaseModel();
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setString(1, carName);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				throw new NullCarException();
			model.setId(resultSet.getInt("ID"));
			model.setName(resultSet.getString("NAME"));
			model.setCount(resultSet.getInt("COUNT"));
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
		return model;
	}
	
	@Override
	public void addCar(String name, int count) throws Exception
	{
		String sqlCommand = "INSERT INTO Car(NAME, COUNT) VALUES(?, ?)";
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setString(1, name);
			statement.setInt(2, count);
			statement.executeUpdate();
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
	}
	
	@Override
	public List<DatabaseModel> listCars() throws Exception
	{
		String sqlCommand = "SELECT * FROM Car ORDER BY ID";
		List<DatabaseModel> carList = new ArrayList<DatabaseModel>();
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				throw new NullListException();
			do
			{
				DatabaseModel model = new DatabaseModel();
				model.setName(resultSet.getString("NAME"));
				model.setId(resultSet.getInt("ID"));
				model.setCount(resultSet.getInt("COUNT"));
				carList.add(model);
			}
			while (resultSet.next());
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
		return carList;
	}

	@Override
	public void updateByName(DatabaseModel model) throws Exception
	{
		String sqlCommand = "UPDATE Car SET COUNT=? WHERE NAME=?";
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setInt(1, model.getCount());
			statement.setString(2, model.getName());
			statement.executeUpdate();
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
	}

	@Override
	public void deleteByName(DatabaseModel model) throws Exception
	{
		String sqlCommand = "DELETE FROM Car WHERE NAME=?";
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setString(1, model.getName());
			statement.executeUpdate();
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
	}
}
