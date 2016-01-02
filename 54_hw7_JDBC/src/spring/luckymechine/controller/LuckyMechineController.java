package spring.luckymechine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import exceptions.NullAccountException;
import spring.luckymechine.account.AccountServer;
import spring.luckymechine.model.LuckyModel;
import viewmodel.HelloModel;

@Controller()
@RequestMapping("/luckymechine")
public class LuckyMechineController
{
	private final String CONTEXT_FILE = "spring/luckymechine/controller/spring.xml";
	private final String DB_ERROR = "error.database";
	private final String ERROR_MODEL = "ErrorModel";
	private final int BASIC_MONEY = 1000;
	private final String DISPLAY_INIT = "== X X X ==<br><br><br>";
	private ResourceBundle resource = ResourceBundle.getBundle("resources.MessageDictionary");
	
	@RequestMapping(value = "/entry", method = RequestMethod.GET)
	public ModelAndView getEntryPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String entryPage = (String)context.getBean("ENTRY_PAGE");
			return new ModelAndView(entryPage);
		}
	}
	
	@RequestMapping(value = "/doEnter", method = RequestMethod.POST)
	public ModelAndView doEnter(@Valid HelloModel helloModel, BindingResult bindingResult)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("MAIN_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			if (bindingResult.hasErrors())
				return new ModelAndView(errorPage, ERROR_MODEL, bindingResult.getFieldErrors());
			String name = helloModel.getName();
			LuckyModel model = new LuckyModel();
			try
			{
				AccountServer accountServer = new AccountServer();
				try
				{
					model = accountServer.findAccount(name);
				}
				catch (NullAccountException exception)
				{
					model.setName(name);
					model.setDollars(BASIC_MONEY);
					accountServer.createAccount(name, BASIC_MONEY);
				}
			}
			catch (Exception e) { return onDatabaseError(e, errorPage, DB_ERROR); };
			model.setDisplay(DISPLAY_INIT);
			return new ModelAndView(successPage, "LuckyModel", model);
		}
	}
	
	@RequestMapping(value = "/doPlay", method = RequestMethod.POST)
	public ModelAndView doPlay(LuckyModel costModel)
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("MAIN_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			LuckyModel model;
			try
			{
				AccountServer accountServer = new AccountServer();
				model = accountServer.findAccount(costModel.getName());
				model = playMechine(model);
				model.addDollars(-costModel.getDollars());
				accountServer.updateAccount(model);
			}
			catch (Exception e) { return onDatabaseError(e, errorPage, DB_ERROR); }
			return new ModelAndView(successPage, "LuckyModel", model);
		}
	}
	
	@RequestMapping(value = "/richrank", method = RequestMethod.GET)
	public ModelAndView getRichRankPage()
	{
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE))
		{
			String successPage = (String)context.getBean("RICH_RANK_PAGE");
			String errorPage = (String)context.getBean("ERROR_PAGE");
			List<LuckyModel> richRank;
			try 
			{
				AccountServer accountServer = new AccountServer();
				richRank = accountServer.getRichRank();
			}
			catch (Exception e) { return onDatabaseError(e, errorPage, DB_ERROR); }
			return new ModelAndView(successPage, "LuckyModelList", richRank);
		}
	}
	
	private LuckyModel playMechine(LuckyModel model)
	{
		Random random = new Random();
		final String symbolSet = "@#%$&7";
		char[] c = new char[3];
		String display = "== ";
		int bonus = 0;
		for (int i = 0; i < c.length; i++)
		{
			c[i] = symbolSet.charAt(random.nextInt(symbolSet.length()));
			display += c[i] + " ";
	    }
		display += "==<br>";
		if (c[0] == c[1] || c[1] == c[2] || c[0] == c[2])
		{
			if (c[0] == c[1] && c[1] == c[2])
			{
				if (c[0] == '7')
				{
					display += "<br>Lucky 7!";
					bonus = 1000000;
				}
				else
				{
					display += "<br>Big Price!";
					bonus = 10000 * symbolSet.indexOf(c[0]);
				}
			}
			else
			{
				display += "<br>A Pair!";
				bonus = 10 * (symbolSet.indexOf(c[0]) + 1);
			}
			display += "<br>You got " + bonus + " dollars.";
		}
		else { display += "<br><br>"; }
		model.addDollars(bonus);
		model.setDisplay(display);
		return model;
	}

	private ModelAndView onDatabaseError(Exception e, String errorPage, String error)
	{
		List<FieldError> feeErrors = new ArrayList<FieldError>();
		String className = this.getClass().getName();
		String errorTip = resource.getString(error) + "<br>" + e.getMessage();
		feeErrors.add(new FieldError(className, error, errorTip));
		return new ModelAndView(errorPage, ERROR_MODEL, feeErrors);
	}
}
