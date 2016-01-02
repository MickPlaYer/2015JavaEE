package action.struts;

import com.opensymphony.xwork2.ActionSupport;

import service.mail.*;
import service.message.*;

public class HelloAction extends ActionSupport
{
	private HelloForm _helloForm;
	
	public HelloForm getHelloForm()
	{
		return _helloForm;
	}
	
	public void setHelloForm(HelloForm helloForm)
	{
		_helloForm = helloForm;
	}
	
	public String doHello()
	{
		Message message = new HelloMessage();
		// Message message = new HiMessage();
		// Message message = new AlohaMessage();
		
		_helloForm.setResult(message.doHello(_helloForm.getName()));
		
		Mail mail = new GoogleMail();
		_helloForm.setStamp(mail.sendMail(_helloForm.getResult()));
		
		return SUCCESS;
	}
}
