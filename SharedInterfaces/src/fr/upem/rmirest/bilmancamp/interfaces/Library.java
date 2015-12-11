package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Warning ! Server security : All Book's methods invoked here must not throw
 * any exceptions.
 *
 */
public interface Library extends Remote {

	// TODO Image is a serializable interface.
	/**
	 * Add a new {@link Book} to the current {@link Library}.
	 * 
	 * @param title
	 *            the book's title.
	 * @param authors
	 *            a list of the book's authors.
	 * @param summary
	 *            the abstract of the book.
	 * @param mainImage
	 *            the {@link Image} of the book's presentation.
	 * @param secondaryImages
	 * @param categories
	 *            a list of strings which represents the categories of the book.
	 * @param price
	 *            the price of the book.
	 * @param tags
	 *            a list of words related to the book.
	 * @throws IllegalArgumentException
	 *             if one of the arguments is <code>null</code> or invalid.
	 * @throws RemoteException
	 */
	public void addBook(String title, List<String> authors, String summary, Image mainImage,
			List<Image> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException, RemoteException;

	/**
	 * Register a new user in the library.
	 * 
	 * @param status
	 *            the status of the user.
	 * @param firstName
	 * @param lastName
	 * @param cardNumber
	 *            the unique library's card number of the user.
	 * @param password
	 *            user's password.
	 * @return <code>true</code> if the registration was successful,
	 *         <code>false</code> otherwise (user already existing).
	 * @throws IllegalArgumentException
	 *             if one of the arguments is <code>null</code> or invalid.
	 * @throws RemoteException
	 */
	public boolean addUser(String status, String firstName, String lastName, int cardNumber, String password)
			throws IllegalArgumentException, RemoteException;

	/**
	 * Connect a user using it's id and it's password.
	 * 
	 * @param id
	 *            the unique id of the user, composed of it's name and it's card
	 *            number.
	 * @param password
	 *            the user's password.
	 * @return a {@link User} object which allows interactions with the books.
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public User connect(String id, String password) throws IllegalArgumentException, RemoteException;

	/**
	 * connect an user and keeps in memory his callback interface
	 * 
	 * @param id
	 *            the unique id of the user, composed of it's name and it's card
	 *            number.
	 * @param password
	 * @param callback
	 * @return a {@link User} object which allows interactions with the books.
	 */
	public User connect(String id, String password, MailBox<Book> callback)
			throws IllegalArgumentException, RemoteException;

	/**
	 * Search a {@link Book} using some keywords, which can be tags, categories,
	 * author, or part of the title.
	 * 
	 * @param keywords
	 *            the list of keywords used to search.
	 * @return a list of {@link Book} which corresponds to the keywords.
	 * @throws RemoteException
	 */
	public List<Book> searchBooks(String... keywords) throws RemoteException;

	/**
	 * Get the existing categories of books in the library.
	 * 
	 * @return the list of categories.
	 * @throws RemoteException
	 */
	public List<String> getCategories() throws RemoteException;

	/**
	 * Get the description of a book category.
	 * 
	 * @param category
	 *            the category name.
	 * @return the description of the given category or {@code null} if the
	 *         category does not exists.
	 * @throws RemoteException
	 */
	public String getCategoryDescription(String category) throws RemoteException;

	/**
	 * Get the recognized {@link User}'s status for the current {@link Library}.
	 * 
	 * @return a list of recognized status.
	 * @throws RemoteException
	 */
	public List<String> getStatus() throws RemoteException;

	/**
	 * Get all the {@link Book}s which are of the given category.
	 * 
	 * @param category
	 *            the category of {@link Book}s which are researched.
	 * @return a list of {@link Book}s of the given category.
	 * @throws RemoteException
	 */
	public List<Book> getCategoryBooks(String category) throws RemoteException;

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most recently added books.
	 * @throws RemoteException
	 */
	public List<Book> getMoreRecentBooks(int number) throws RemoteException;

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> first books based of book's
	 *         evaluations.
	 * @throws RemoteException
	 */
	public List<Book> getBestRatedBooks(int number) throws RemoteException;

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most borrowed books.
	 * @throws RemoteException
	 */
	public List<Book> getMostConsultedBooks(int number) throws RemoteException;

	/**
	 * Get the <code>number</code> books the most similar to the given one.
	 * 
	 * @param book
	 *            the comparaison book.
	 * @param number
	 *            the wanted number of books.
	 * @return a list of the <code>number</code> books the most similar to
	 *         <code>book</code>.
	 * @throws RemoteException
	 */
	public List<Book> getMostSimilarsBooks(Book book, int number) throws RemoteException;

	/**
	 * Try to borrow the given {@link Book}. If not available, then user will be
	 * notify later once available.
	 * 
	 * @param book
	 *            the {@link Book} to borrow
	 * @param user
	 *            the {@link User} borrowing the {@link Book}
	 * @return <code>true</code> if available otherwise <code>false</code>
	 * @throws RemoteException
	 */
	public boolean borrow(Book book, User user) throws RemoteException;

	/**
	 * Allow the given {@link User} to give back the given {@link Book} if he
	 * borrowed it previously.
	 * 
	 * @param book
	 *            the book to give back.
	 * @param user
	 *            the user to give back the book.
	 * @throws RemoteException
	 */
	public boolean giveBack(Book book, User user) throws RemoteException;

	/**
	 * Give an evaluation to the given {@link Book} from given {@link User}
	 * 
	 * @param book
	 *            the book to rate
	 * @param value
	 *            The rate value
	 * @return <code>true</code> if operatio succeeds otherwise
	 *         <code>false</code>
	 * @throws RemoteException
	 */
	public boolean rateBook(Book book, User user, int value) throws RemoteException;

	/**
	 * Get a list of all {@link Book}s borrowed by the given {@link User}.
	 * 
	 * @param user
	 * 
	 * @return a list of all the books the given user have borrowed.
	 * @throws RemoteException
	 */
	public List<Book> getBookHistory(User user) throws RemoteException;

	/**
	 * disconnect given user
	 * 
	 * @param user
	 * @throws RemoteException
	 */
	public void disconnect(User user) throws RemoteException;

}
