package fr.upem.rmirest.bilmancamp.ws.selling;

import java.rmi.RemoteException;

public interface Selling {

	/**
	 * Sell a book to an user.
	 * 
	 * @param id
	 *            the bank account id.
	 * @param password
	 *            the bank password.
	 * @param price
	 *            the price of the sold book.
	 * @param currency
	 *            the currency used for the transaction.
	 * @return <code>true</code> if the book was successfully sold,
	 *         <code>false</code> otherwise.
	 * @throws RemoteException
	 */
	public boolean sellBook(long id, String password, double price, String currency) throws RemoteException;

	/**
	 * Get the change from a devise to another.
	 * 
	 * @param currencyFrom
	 *            the devise of the given amount.
	 * @param currencyTo
	 *            the devise the amount will be converted to.
	 * @param amount
	 *            the amount to convert from the first devise to the second.
	 * @return the converted amount.
	 * @throws RemoteException
	 * @throws IllegalArgumentException
	 */
	public double change(String currencyFrom, String currencyTo, double amount)
			throws RemoteException, IllegalArgumentException;
}
