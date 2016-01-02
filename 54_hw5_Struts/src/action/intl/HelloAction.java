package action.intl;



import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

import service.mail.*;
import service.message.Message;

public class HelloAction extends ActionSupport
{
	private final String FIELD_ERROR_ERROR_NAME = "errorName";
	private final String FIELD_ERROR_NULL_NAME = "nullName";
	private final String ERROR_CHAR_ONLY = "error.charOnly";
	private final String ERROR_EMPTY = "error.empty";
	private HelloForm _helloForm;
	
	// 提供表單輸出開口
	public HelloForm getHelloForm()
	{
		return _helloForm;
	}
	
	// 提供表單注入開口
	public void setHelloForm(HelloForm helloForm)
	{
		_helloForm = helloForm;
	}
	
	// 自訂函式，透過struts.xml設定呼叫來源
	public String doHello()
	{
		// 從spring.xml取得Server Work
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("service/message/spring.xml"))
		{
			Message message = (Message)context.getBean("message");
			_helloForm.setResult(message.doHello(_helloForm.getName()));
		}
		// Mail
		Mail mail = new GoogleMail();
		_helloForm.setStamp(mail.sendMail(_helloForm.getResult()));
		// 回傳success標籤，再透過struts.xml選擇跳至新頁面
		return SUCCESS;
	}
	
	// 複寫validate函式，新增自訂偵錯規則
	@Override
	public void validate()
	{
		// 取得資源檔，內含錯誤提示文字
		if (_helloForm.getName().length() != 0)
		{
			// 規則：名稱只能為中文字或英文字母
			boolean isLetter = _helloForm.getName().matches("[A-Za-z]+");
			boolean isCharacter = _helloForm.getName().matches("[\u4E00-\u9FA5]");
			if (!isLetter && !isCharacter)
				this.addFieldError(FIELD_ERROR_ERROR_NAME, getText(ERROR_CHAR_ONLY));
		}
		else
		{
			// 規則：名稱不能為空
			this.addFieldError(FIELD_ERROR_NULL_NAME, getText(ERROR_EMPTY));
		}
		// 呼叫父項validate函式
		super.validate();
	}
}
