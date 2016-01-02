package service.fee;

import java.util.List;

import model.FeeModel;

public interface Fee
{
	void create(FeeModel feeModel) throws Exception;
	void update(FeeModel feeModel) throws Exception;
	void delete(FeeModel feeModel) throws Exception;
	FeeModel find(FeeModel feeModel) throws Exception;
	List<FeeModel> list() throws Exception;
}
