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
	 * @return the main {@link Image} of the current book.
	 */
	public Image getMainImage() throws RemoteException;

	/**
	 * @return a list of secondary {@link Image}s of the current book.
	 */
	public List<Image> getSecondaryImages() throws RemoteException;

	/**
	 * @return a list of all the comments that have been posted about the book.
	 */
	public List<BookComment> getComments() throws RemoteException;

	// TODO ajouter les autres getters de je suis fatigu� loool.
	// Tu les trouveras en faisant la classe d'implantation !
	// Je checkerai après mon cours,,,,

	/**
	 * @return <code>true</code> if the book can be borrowed.
	 */
	public boolean isAvailable() throws RemoteException;

	/**
	 * Adds a commentary to the current {@link Book}.
	 * 
	 * @param bookComment
	 *            the commentary about the current book.
	 */
	public void comment(BookComment bookComment) throws RemoteException;

	/**
	 * Unregister the given {@link User} from the reservation queue of the
	 * current {@link Book}.
	 * 
	 * @param user
	 *            the {@link User} who unregister the reservation queue.
	 */
	public void unregister(User user) throws RemoteException;

	/**
	 * Allow the given {@link User} to know it's position into the reservation
	 * queue of the current {@link Book}.
	 * 
	 * @param user
	 *            the {@link User} who wants to know it's position in the
	 *            current {@link Book}'s reservation queue.
	 * @return the position of the given {@link User} in the current
	 *         {@link Book}'s reservation queue.
	 */
	public int getRankInWaitingQueue(User user) throws RemoteException;
}
