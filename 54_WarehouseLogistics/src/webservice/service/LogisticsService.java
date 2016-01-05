package webservice.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import webservice.requestmodel.AutoDeliverModel;

@RestController()
@RequestMapping("/webservice/logistics")
public class LogisticsService
{
	@RequestMapping(value = "/auto", method = RequestMethod.POST)
	public String getMyBoxList(@RequestBody AutoDeliverModel model) throws Exception
	{
		return "Ok";
	}
}
