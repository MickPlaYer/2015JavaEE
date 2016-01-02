package test2.service.message;

import java.util.ResourceBundle;

import test2.viewmodel.ThankModel;

public class ThankMessage implements Message
{
	public String doMessage(ThankModel model)
	{
		ResourceBundle resource = ResourceBundle.getBundle("test2.resources.MessageDictionary");
		String result = "";
		result += resource.getString("thankCard.title") + "\n";
		result += resource.getString("thankCard.thankTo") + model.getName() + "\n";
		result += resource.getString("thankCard.count") + model.getCount();
		return result;
	}
}
