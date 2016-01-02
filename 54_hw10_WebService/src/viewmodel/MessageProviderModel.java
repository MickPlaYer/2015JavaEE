package viewmodel;

public class MessageProviderModel {
	private MessageModel messageModel;
	private String messageProvider;
	private String mailProvider;
	private String feeProvider;
	
	public MessageModel getMessageModel() {
		return messageModel;
	}
	public void setMessageModel(MessageModel messageModel) {
		this.messageModel = messageModel;
	}
	public String getMessageProvider() {
		return messageProvider;
	}
	public void setMessageProvider(String messageProvider) {
		this.messageProvider = messageProvider;
	}
	public String getMailProvider() {
		return mailProvider;
	}
	public void setMailProvider(String mailProvider) {
		this.mailProvider = mailProvider;
	}
	public String getFeeProvider() {
		return feeProvider;
	}
	public void setFeeProvider(String feeProvider) {
		this.feeProvider = feeProvider;
	}
}
