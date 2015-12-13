package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Warning ! Server security : All User's methods invoked here must not throw
 * any exceptions.
 *
 */
public interface Book extends Remote {

	// Getters de base du livre.

	/**
	 * Get the database related id of the current Book.
	 * 
	 * @return the unique id in the database.
	 */
	public int getId() throws RemoteException;

	/**
	 * Get the creation {@link Date} of the current book.
	 * 
	 * @return the date of the book creation.
	 */
	public LocalDate getDate() throws RemoteException;

	/**
	 * @return the current book's title.
	 */
	public String getTitle() throws RemoteException;

	/**
	 * @return a list of the current book's authors.
	 */
	public List<String> getAuthors() throws RemoteException;

	/**
	 * @return the current book's summary.
	 */
	public String getSummary() throws RemoteException;

	/**
	 * @return a list of the categories of the current book.
	 */
	public List<String> getCategories() throws RemoteException;

	/**
	 * @return how many times the current {@link Book} was borrowed.
	 */
	public int getConsultationNumber() throws RemoteException;

	/**
	 * @return the price of the current book.
	 */
	public double getPrice() throws RemoteException;

	/**
	 * @return the average evaluation of the current book.
	 */
	public float getRate() throws RemoteException;

	/**
	 * @return the number of evaluations of the current book.
	 */
	public int getRateNumber() throws RemoteException;

	/**
	 * @return a list of the current book's tags.
	 */
	public List<String> getTags() throws RemoteException;

	/**
	 * @return the main image of the current book as a base64 {@link String}.
	 */
	public String getMainImage() throws RemoteException;

	/**
	 * @return a list of secondary images of the current book, encoded as base64
	 *         strings. 
	 */
	@Deprecated
	public List<String> getSecondaryImages() throws RemoteException;

	/**
	 * @return a list of all the comments that have been posted about the book.
	 */
	public List<BookComment> getComments() throws RemoteException;

}
