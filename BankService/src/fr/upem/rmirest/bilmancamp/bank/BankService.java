package fr.upem.rmirest.bilmancamp.bank;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import fr.upem.rmirest.bilmancamp.bankdatabase.BankDatabase;
import fr.upem.rmirest.bilmancamp.bankdatabase.BankDatabaseImpl;
import net.restfulwebservices.www.DataContracts._2008._01.Currency;
import net.restfulwebservices.www.DataContracts._2008._01.CurrencyCode;
import net.restfulwebservices.www.ServiceContracts._2008._01.CurrencyServiceLocator;
import net.restfulwebservices.www.ServiceContracts._2008._01.ICurrencyService;

public class BankService implements Bank {

	private final BankDatabase database = new BankDatabaseImpl();

	@Override
	public long createAccount(String currency, String password) {
		try {
			CurrencyCode.fromString(currency);
		} catch (IllegalArgumentException e) {
			return 0;
		}
		return database.createAccount(currency, password);
	}

	@Override
	public boolean deposit(long id, String password, String currency, double amount) {
		if (!database.accessValid(id, password)) {
			return false;
		}
		if (amount < 0) {
			return false;
		}
		double accountChange;
		try {
			accountChange = change(database.getAccountCurrency(id), currency, amount);
		} catch (RemoteException | IllegalArgumentException e) {
			return false;
		}
		return database.deposit(id, accountChange);
	}

	@Override
	public boolean withdraw(long id, String password, String currency, double amount) {
		if (!database.accessValid(id, password)) {
			return false;
		}
		if (amount < 0) {
			return false;
		}
		double accountChange;
		try {
			accountChange = change(database.getAccountCurrency(id), currency, amount);
		} catch (RemoteException e) {
			return false;
		}
		return database.withdraw(id, accountChange);
	}

	@Override
	public double balance(long id, String password, String currency) {
		if (!database.accessValid(id, password)) {
			throw new IllegalStateException("Invalid credentials");
		}
		double balance = database.balance(id);
		try {
			return change(database.getAccountCurrency(id), currency, balance);
		} catch (RemoteException e) {
			throw new IllegalArgumentException("Account unreachable");
		}
	}

	@Override
	public double change(String currencyFrom, String currencyTo, double amount)
			throws RemoteException, IllegalArgumentException {
		ICurrencyService currencyService;
		try {
			currencyService = new CurrencyServiceLocator().getBasicHttpBinding_ICurrencyService();

			Currency conversionRate = currencyService.getConversionRate(CurrencyCode.fromString(currencyFrom),
					CurrencyCode.fromString(currencyTo));
			return conversionRate.getRate() * amount;
		} catch (ServiceException e) {
			throw new RemoteException("An error occured in the service.");
		}
	}

}
