package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;
import java.util.List;

public interface User extends Remote {

	/**
	 * Get the database related id of this User.
	 * 
	 * @return the unique id in the database.
	 */
	public int getId();

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
	 * Get a list of all borrowed books.
	 * 
	 * TODO Maybe move as Library::getBookHistory(User) for database access.
	 * 
	 * @return a list of all the books the current user have borrowed.
	 */
	public List<Book> getBookHistory();

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
