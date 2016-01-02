package viewmodel;

import model.FeeModel;

public class FeeProviderModel {
	private FeeModel feeModel;
	private String feeProvider;
	
	public FeeModel getFeeModel() {
		return feeModel;
	}
	public void setFeeModel(FeeModel feeModel) {
		this.feeModel = feeModel;
	}
	public String getFeeProvider() {
		return feeProvider;
	}
	public void setFeeProvider(String feeProvider) {
		this.feeProvider = feeProvider;
	}
}
