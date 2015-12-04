package fr.upem.rmirest.bilmancamp.interfaces;

public interface User {

	/**
	 * @return the status of the current user.
	 */
	public String getStatus();

	/**
	 * @return the current user's first name.
	 */
	public String getFirstName();

	/**
	 * @return the current user's last name.
	 */
	public String getLastName();

	/**
	 * @return the current user's library card number.
	 */
	public int getCardNumber();

	/**
	 * Indicates if the given credentials are valid for the current user.
	 * 
	 * @param id
	 *            the id of the user which is firstName + lastName + cardNumber
	 *            concatenate.
	 * @param password
	 *            the encrypted password.
	 * @return true if the given connection data are valid for the current user.
	 */
	public boolean isLoginValid(String id, String password);

	/**
	 * Disconnect the current user.
	 */
	public void disconnect();

}
