package action.car.struts;

import com.opensymphony.xwork2.ActionSupport;

import carrace.race.Race;
import service.mail.GoogleMail;
import service.mail.Mail;

public class PickCarAction extends ActionSupport
{
	private CarForm _carForm = new CarForm();
	
	public CarForm getCarForm()
	{
		return _carForm;
	}
	
	public void setCarForm(CarForm carForm)
	{
		_carForm = carForm;
	}
	
	public String pickCar()
	{
		String email = _carForm.getEmail();
		String carPick = _carForm.getCarPick();
		String detail;
		String report;
		String stamp;
		
		Race race = new Race(carPick);
		race.Start();
		detail = race.getDetail();
		report = race.getReport();
		
		Mail mail = new GoogleMail();
		stamp = mail.sendMail(detail, email);
		
		_carForm.setReport(report);
		_carForm.setStamp(stamp);
		return SUCCESS;
	}
}
