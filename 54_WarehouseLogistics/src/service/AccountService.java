package service;

import javax.servlet.http.HttpSession;

import model.AccountModel;
import service.database.AccountDatabase;
import exceptions.SessionFailException;

public class AccountService
{
	public AccountModel sessionCheck(HttpSession httpSession) throws Exception
	{
		AccountDatabase accountDatabase = new AccountDatabase();
		AccountModel account;
		String name = (String)httpSession.getAttribute("name");
		String session = (String)httpSession.getAttribute("session");
		if (name == null || session == null)
			throw new SessionFailException();
		account = accountDatabase.findByName(name);
		if  (!account.getSession().equals(session))
			throw new SessionFailException();
		return account;
	}
}
