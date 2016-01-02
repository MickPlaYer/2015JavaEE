package test1.service.database;

import java.util.List;

import test1.model.DatabaseModel;

public interface CarDatabase
{
	DatabaseModel findByName(String carName) throws Exception;
	void addCar(String carName, int count) throws Exception;
	List<DatabaseModel> listCars() throws Exception;
	void updateByName(DatabaseModel model) throws Exception;
	void deleteByName(DatabaseModel model) throws Exception;
}
