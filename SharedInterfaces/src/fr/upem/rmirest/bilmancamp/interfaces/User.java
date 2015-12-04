package fr.upem.rmirest.bilmancamp.interfaces;

public interface User {

	/**
	 * @return the status of the user.
	 */
	public String getStatus();

	/**
	 * @return the user's first name.
	 */
	public String getFirstName();

	/**
	 * @return the user's last name.
	 */
	public String getLastName();

	/**
	 * @return the user's library card number.
	 */
	public int getCardNumber();

	/**
	 * Indicates if the connection data are valid for this user.
	 * 
	 * @param id
	 *            the id of the user which is firstName + lastName + cardNumber
	 *            concatenate.
	 * @param password
	 *            the encrypted password.
	 * @return true if the given connection data are valid for this user.
	 */
	public boolean isLoginValid(String id, String password);

	/**
	 * Disconnect the user. Standard values are still accessible, but not
	 * interactions with the library.
	 */
	public void disconnect();

}
