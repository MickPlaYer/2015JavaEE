package spring.luckymechine.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.FeeModel;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import exceptions.NullAccountException;
import exceptions.NullListException;
import spring.luckymechine.model.LuckyModel;

public class AccountServer
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private DataSource _feeDataSource;
	
	public AccountServer() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			InitialContext contextJNDI = new InitialContext();
			_feeDataSource = (DataSource)contextJNDI.lookup((String)context.getBean("DataSource"));
		}
		catch (Exception exception) { throw exception; }
	}
	
	public LuckyModel findAccount(String name) throws Exception
	{
		String sqlCommand = "SELECT * FROM LuckyAccount WHERE NAME=?";
		LuckyModel luckyModel = new LuckyModel();
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				throw new NullAccountException();
			luckyModel.setId(resultSet.getInt("ID"));
			luckyModel.setName(resultSet.getString("NAME"));
			luckyModel.setDollars(resultSet.getInt("DOLLARS"));
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
		return luckyModel;
	}
	
	public void createAccount(String name, int dollars) throws Exception
	{
		String sqlCommand = "INSERT INTO LuckyAccount(NAME, DOLLARS) VALUES(?, ?)";
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setString(1, name);
			statement.setInt(2, dollars);
			statement.executeUpdate();
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
	}
	
	public void updateAccount(LuckyModel luckyModel) throws Exception
	{
		String sqlCommand = "UPDATE LuckyAccount SET DOLLARS=? WHERE ID=?";
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			statement.setInt(1, luckyModel.getDollars());
			statement.setInt(2, luckyModel.getId());
			statement.executeUpdate();
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
	}
	
	public List<LuckyModel> getRichRank() throws Exception
	{
		String sqlCommand = "SELECT * FROM LuckyAccount ORDER BY dollars DESC";
		List<LuckyModel> richRank = new ArrayList<LuckyModel>();
		try
		{
			Connection dataBase = _feeDataSource.getConnection();
			PreparedStatement statement = dataBase.prepareStatement(sqlCommand);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				throw new NullListException();
			do
			{
				LuckyModel luckyModel = new LuckyModel();
				luckyModel.setName(resultSet.getString("NAME"));
				luckyModel.setId(resultSet.getInt("ID"));
				luckyModel.setDollars(resultSet.getInt("DOLLARS"));
				richRank.add(luckyModel);
			}
			while (resultSet.next());
			statement.close();
			dataBase.close();
		}
		catch (Exception exception) { throw exception; }
		return richRank;
	}
}
