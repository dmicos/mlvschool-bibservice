package fr.upem.rmirest.bilmancamp.bank;

import java.rmi.RemoteException;

public interface Bank {

	/**
	 * Create an account into the database.
	 * 
	 * @param currency
	 *            the currency to be used as main currency of the account.
	 * @param password
	 *            the account password.
	 * @return the account id of the newly created account, or {@code 0} if the
	 *         creation failed.
	 */
	public long createAccount(String currency, String password);

	/**
	 * Add the given amount to the account corresponding to the given id.
	 * 
	 * @param id
	 *            the account id.
	 * @param password
	 *            the account password.
	 * @param currency
	 *            the currency of the operation.
	 * @param amount
	 *            the amount to add.
	 * @return <code>true</code> if the operation was successful,
	 *         <code>false</code> otherwise.
	 */
	public boolean deposit(long id, String password, String currency, double amount);

	/**
	 * Withdraw the given amount from the account corresponding to the given id.
	 * 
	 * @param id
	 *            the account id.
	 * @param password
	 *            the account password.
	 * @param currency
	 *            the currency of the operation.
	 * @param amount
	 *            the amount to withdraw.
	 * @return <code>true</code> if the balance was sufficient and the withdraw
	 *         effective, <code>false</code> otherwise.
	 */
	public boolean withdraw(long id, String password, String currency, double amount);

	/**
	 * 
	 * @param id
	 *            the account id.
	 * @param password
	 *            the account password.
	 * @param currency
	 * @return the actual balance of the account corresponding to the given id,
	 *         in the given currency.
	 */
	public double balance(long id, String password, String currency);

	/**
	 * Get the change from a devise to another.
	 * 
	 * @param deviseFrom
	 *            the devise of the given amount.
	 * @param deviseTo
	 *            the devise the amount will be converted to.
	 * @param amount
	 *            the amount to convert from the first devise to the second.
	 * @return the converted amount.
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 * @throws DefaultFaultContract
	 */
	public double change(String currencyFrom, String currencyTo, double amount)
			throws RemoteException, IllegalArgumentException;

}
