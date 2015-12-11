package fr.upem.rmirest.bilmancamp.bankdatabase;

public interface BankDatabase {

	/**
	 * Create an account into the database.
	 * 
	 * @param currency
	 *            the main currency of the account.
	 * @return the account id of the newly created account, or {@code 0} if the
	 *         creation failed.
	 */
	public long createAccount(String currency, String password);

	/**
	 * Check the validity of the given credentials.
	 * 
	 * @param id
	 *            the account id.
	 * @param password
	 *            the account password.
	 * @return <code>true</code> if the credentials are valid,
	 *         <code>false</code> otherwise.
	 */
	public boolean accessValid(long id, String password);

	/**
	 * Add the given amount to the account corresponding to the given id.
	 * 
	 * @param id
	 *            the account id.
	 * @param amount
	 *            the amount to add.
	 * @return <code>true</code> if the operation was successful,
	 *         <code>false</code> otherwise.
	 */
	public boolean deposit(long id, double amount);

	/**
	 * Withdraw the given amount from the account corresponding to the given id.
	 * 
	 * @param amount
	 *            the amount to withdraw.
	 * @return <code>true</code> if the balance was sufficient and the withdraw
	 *         effective, <code>false</code> otherwise.
	 */
	public boolean withdraw(long id, double amount);

	/**
	 * @param id
	 * @return the actual balance of the account corresponding to the given id.
	 */
	public double balance(long id);

	/**
	 * @param id
	 *            the id of the account.
	 * @return the main currency of the account corresponding to the given id.
	 */
	public String getAccountCurrency(long id);

}
