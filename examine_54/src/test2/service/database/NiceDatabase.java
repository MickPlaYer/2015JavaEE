package test2.service.database;

import java.util.List;

import test2.model.DatabaseModel;

public interface NiceDatabase
{
	DatabaseModel findByName(String carName) throws Exception;
	void addNiceUser(String carName, int count) throws Exception;
	List<DatabaseModel> listNiceUsers() throws Exception;
	void updateByName(DatabaseModel model) throws Exception;
	void deleteByName(DatabaseModel model) throws Exception;
}
