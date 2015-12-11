package fr.upem.rmirest.bilmancamp.selling;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import fr.upem.rmirest.bilmancamp.bank.BankService;
import fr.upem.rmirest.bilmancamp.bank.BankServiceServiceLocator;

public class SellingService implements Selling {

	@Override
	public boolean sellBook(long id, String password, double price, String currency) throws RemoteException {
		try {
			BankService bank = getBank();
			return bank.withdraw(id, password, currency, price);
		} catch (ServiceException e) {
			throw new IllegalStateException("Bank unreachable.");
		}
	}

	@Override
	public double change(String currencyFrom, String currencyTo, double amount)
			throws RemoteException, IllegalArgumentException {
		try {
			return getBank().change(currencyFrom, currencyTo, amount);
		} catch (ServiceException e) {
			throw new IllegalStateException("Bank unreachable.");
		}
	}

	private BankService getBank() throws ServiceException {
		return new BankServiceServiceLocator().getBankService();
	}

}
