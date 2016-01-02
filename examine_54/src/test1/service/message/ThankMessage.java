package test1.service.message;

import java.util.ResourceBundle;

import test1.viewmodel.SellCarModel;

public class ThankMessage implements Message
{
	public String doMessage(SellCarModel model)
	{
		ResourceBundle resource = ResourceBundle.getBundle("test1.resources.MessageDictionary");
		String result = "";
		result += resource.getString("test1.buyername") + ": " + model.getBuyer() + "\n";
		result += resource.getString("test1.carname") + ": " + model.getName() + "\n";
		result += resource.getString("test1.sell.thanks");
		return result;
	}
}
