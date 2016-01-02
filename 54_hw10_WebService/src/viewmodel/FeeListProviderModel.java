package viewmodel;

import java.util.List;

import model.FeeModel;

public class FeeListProviderModel {
	private List<FeeModel> feeListModel;
	private String feeProvider;
	
	public List<FeeModel> getFeeListModel() {
		return feeListModel;
	}
	public void setFeeListModel(List<FeeModel> feeListModel) {
		this.feeListModel = feeListModel;
	}
	public String getFeeProvider() {
		return feeProvider;
	}
	public void setFeeProvider(String feeProvider) {
		this.feeProvider = feeProvider;
	}
}
