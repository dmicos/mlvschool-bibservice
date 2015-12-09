package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface User extends Remote {

	/**
	 * Get the database related id of this User.
	 * 
	 * @return the unique id in the database.
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;

	/**
	 * @return the status of the current user.
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;

	/**
	 * @return the current user's first name.
	 * @throws RemoteException
	 */
	public String getFirstName() throws RemoteException;

	/**
	 * @return the current user's last name.
	 * @throws RemoteException
	 */
	public String getLastName() throws RemoteException;

	/**
	 * @return the current user's library card number.
	 * @throws RemoteException
	 */
	public int getCardNumber() throws RemoteException;

	/**
	 * Indicates if the given credentials are valid for the current user.
	 * 
	 * @param id
	 *            the id of the user which is firstName + lastName + cardNumber
	 *            concatenate.
	 * @param password
	 *            the encrypted password.
	 * @return true if the given connection data are valid for the current user.
	 * @throws RemoteException
	 */
	public boolean isLoginValid(String id, String password) throws RemoteException;

	/**
	 * Disconnect the current user.
	 * 
	 * @throws RemoteException
	 */
	public void disconnect() throws RemoteException;

}
