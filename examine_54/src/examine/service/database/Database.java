package examine.service.database;

import java.util.List;

import examine.model.CarModel;
import examine.model.OwnerModel;

public interface Database
{
	List<CarModel> getCarlist() throws Exception;

	List<OwnerModel> getOwnerList() throws Exception;

	OwnerModel findOwner(int id) throws Exception;
	
	CarModel findCar(int id) throws Exception;

	/*List<CarModel> getOwnerCars(int ownerId) throws Exception;

	void buildCar(OwnerModel builder, String carName, int buildPrice) throws Exception;
	
	void buyCar(OwnerModel buyer, OwnerModel seller, CarModel car, int payment) throws Exception;*/
}
