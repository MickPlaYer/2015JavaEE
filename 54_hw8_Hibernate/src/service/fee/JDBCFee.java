package service.fee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import exceptions.NullAccountException;
import exceptions.NullListException;
import model.FeeModel;

public class JDBCFee implements Fee
{
	private final String CONTEXT_FILE = "spring/controller/database/spring.xml";
	private DataSource _feeDataSource;
	private Connection _dataBaseConnection;
	private PreparedStatement _countStatement;
	private String _sqlCommand;
	
	public JDBCFee() throws Exception
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			InitialContext contextJNDI = new InitialContext();
			_feeDataSource = (DataSource)contextJNDI.lookup((String)context.getBean("DataSource"));
		}
		catch (Exception exception) { throw exception; }
	}
	
	@Override
	public void create(FeeModel feeModel) throws Exception
	{
		_sqlCommand = "INSERT INTO Fee(NAME, COUNT) VALUES(?, ?)";
		try
		{
			_dataBaseConnection = _feeDataSource.getConnection();
			_countStatement = _dataBaseConnection.prepareStatement(_sqlCommand);
			_countStatement.setString(1, feeModel.getName());
			_countStatement.setInt(2, feeModel.getCount());
			_countStatement.executeUpdate();
			_countStatement.close();
			_dataBaseConnection.close();
		}
		catch (Exception exception) { throw exception; }
	}
	
	@Override
	public void update(FeeModel feeModel) throws Exception
	{
		_sqlCommand = "UPDATE Fee SET COUNT=? WHERE NAME=?";
		try
		{
			_dataBaseConnection = _feeDataSource.getConnection();
			_countStatement = _dataBaseConnection.prepareStatement(_sqlCommand);
			_countStatement.setInt(1, feeModel.getCount());
			_countStatement.setString(2, feeModel.getName());
			_countStatement.executeUpdate();
			_countStatement.close();
			_dataBaseConnection.close();
		}
		catch (Exception exception) { throw exception; }
	}

	@Override
	public void delete(FeeModel feeModel) throws Exception
	{
		_sqlCommand = "DELETE FROM Fee WHERE NAME=?";
		try
		{
			_dataBaseConnection = _feeDataSource.getConnection();
			_countStatement = _dataBaseConnection.prepareStatement(_sqlCommand);
			_countStatement.setString(1, feeModel.getName());
			_countStatement.executeUpdate();
			_countStatement.close();
			_dataBaseConnection.close();
		}
		catch (Exception exception) { throw exception; }
	}

	@Override
	public FeeModel find(FeeModel feeModel) throws Exception
	{
		_sqlCommand = "SELECT * FROM Fee WHERE NAME=?";
		FeeModel newFeeModel = new FeeModel();
		try
		{
			_dataBaseConnection = _feeDataSource.getConnection();
			_countStatement = _dataBaseConnection.prepareStatement(_sqlCommand);
			_countStatement.setString(1, feeModel.getName());
			ResultSet resultSet = _countStatement.executeQuery();
			if (!resultSet.next())
				throw new NullAccountException();
			newFeeModel.setName(feeModel.getName());
			newFeeModel.setId(resultSet.getInt("ID"));
			newFeeModel.setCount(resultSet.getInt("COUNT"));
			_countStatement.close();
			_dataBaseConnection.close();
		}
		catch (Exception exception) { throw exception; }
		return newFeeModel;
	}

	@Override
	public List<FeeModel> list() throws Exception
	{
		_sqlCommand = "SELECT * FROM Fee ORDER BY id";
		List<FeeModel> feeModelList = new ArrayList<FeeModel>();
		try
		{
			_dataBaseConnection = _feeDataSource.getConnection();
			_countStatement = _dataBaseConnection.prepareStatement(_sqlCommand);
			ResultSet resultSet = _countStatement.executeQuery();
			if (!resultSet.next())
				throw new NullListException();
			do
			{
				FeeModel newFeeModel = new FeeModel();
				newFeeModel.setName(resultSet.getString("NAME"));
				newFeeModel.setId(resultSet.getInt("ID"));
				newFeeModel.setCount(resultSet.getInt("COUNT"));
				feeModelList.add(newFeeModel);
			}
			while (resultSet.next());
			_countStatement.close();
			_dataBaseConnection.close();
		}
		catch (Exception exception) { throw exception; }
		return feeModelList;
	}
}
