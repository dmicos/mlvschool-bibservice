package fr.upem.rmirest.bilmancamp.database;

import java.rmi.RemoteException;
import java.util.List;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public interface Database {

	/**
	 * Add the given {@link Book} to the database.
	 * 
	 * @param book
	 *            the book to add.
	 * @return <code> true </code> if operation succeeds otherwise
	 *         <code> false </code>
	 */
	public boolean addBook(BookPOJO book);

	/**
	 * Add the given {@link UserPOJO} to the database.
	 * 
	 * @param user
	 *            the user to add.
	 * @return <code>true</code> if the user was added correctly,
	 *         <code>false</code> otherwise.
	 */
	public boolean addUser(UserPOJO user);

	/**
	 * Add a new {@link UserPOJO} with his password
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean addUser(UserPOJO user, String password);

	/**
	 * Get a {@link UserPOJO} object from the database values, using the given
	 * credentials.
	 * 
	 * @param id
	 *            the user id.
	 * @param password
	 *            the encrypted user password.
	 * @return the {@link UserPOJO} or null if the credentials were invalids.
	 */
	public UserPOJO connectUser(String id, String password);

	/**
	 * Get a list of {@link Book}s that corresponds to one of the keywords.
	 * Correspondences can be from tags, categories, or title part.
	 * 
	 * @param keywords
	 *            an {@link Iterable} of keywords.
	 * @return a list of books that corresponds to one of the keywords.
	 */
	public List<BookPOJO> searchBookFromKeywords(String... keywords);

	/**
	 * Add the given category to the current database.
	 * 
	 * @param category
	 *            the category to add.
	 * @return <code>true</code> if successfully added, <code>false</code>
	 *         otherwise.
	 */
	public boolean addCategory(String category);

	/**
	 * Get all the categories of books in the database.
	 * 
	 * @return a list of all the categories.
	 */
	public List<String> getCategories();

	/**
	 * Get all the {@link BookPOJO}s that are of the given category.
	 * 
	 * @param category
	 * @return a list of books of the given category.
	 */
	public List<BookPOJO> getBookFromCategory(String category);

	/**
	 * Get the <code>number</code> most recent {@link Book}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most recent books.
	 */
	public List<BookPOJO> getBookRecents(int number);

	/**
	 * Get the <code>number</code> best rated {@link Book}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> best rates books.
	 */
	public List<BookPOJO> getBookBestRate(int number);

	/**
	 * Get the <code>number</code> most consulted {@link BookPOJO}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most consulted books.
	 */
	public List<BookPOJO> getBookMostConsulted(int number);

	/**
	 * Get the <code>number</code> books the most similar to the given one.
	 * 
	 * @param book
	 *            the comparison book.
	 * @param number
	 *            the wanted number of books.
	 * @return a list of the <code>number</code> books the most similar to
	 *         <code>book</code>.
	 */
	public List<BookPOJO> getBookMostSimilar(BookPOJO book, int number);

	/**
	 * Get every book ever borrowed by the given user.
	 * 
	 * @param user
	 * @return a list of {@link BookPOJO} that represents the books borrowed by
	 *         the given user.
	 */
	public List<BookPOJO> getBorrowedBooks(UserPOJO user);

	/**
	 * Try to borrow the given {@link BookPOJO}. If not available, then user
	 * will notify later once available
	 * 
	 * @param book
	 *            the {@link BookPOJO} to borrow
	 * @param user
	 *            the {@link UserPOJO} borrowing the {@link Book}
	 * @return <code>true</code> if available otherwise <code>false</code>
	 */
	public boolean borrow(BookPOJO book, UserPOJO user);

	/**
	 * Get a list of all the books actually borrowed by the given {@link User}.
	 * 
	 * @param userPOJO
	 * @return
	 */
	public List<BookPOJO> getBooks(UserPOJO userPOJO);

	/**
	 * Get a list of the {@link Book}s the given user is waiting for.
	 * 
	 * @param userPOJO
	 * @return a list of the {@link Book}s the given user is waiting for.
	 */
	public List<BookPOJO> getPendingBooks(UserPOJO userPOJO);

	/**
	 * Check if the given {@link Book}is available
	 * 
	 * @param book
	 *            the {@link Book} to check
	 * @return <code>true</code> if available otherwise <code>false</code>
	 */
	public boolean isAvailable(BookPOJO book);

	/**
	 * Get the <code>limit</code> waiters
	 * 
	 * @param book
	 *            the {@link BookPOJO} to borrow
	 * @return a list of {@link UserPOJO}
	 */
	public List<UserPOJO> getQueue(BookPOJO book, int limit);

	/**
	 * Add given {@link User} to the {@link Book} queue
	 * 
	 * @param user
	 *            The {@link User} who wants to borrow
	 * @param book
	 *            The {@link Book} to borrow
	 * 
	 * @return <code>true</code> if succeed otherwise <code>false</code>
	 */
	public boolean addToQueue(UserPOJO user, BookPOJO book);

	/**
	 * Give an evaluation to the given {@link BookPOJO} from given
	 * {@link UserPOJO}
	 * 
	 * @param book
	 *            the book to rate
	 * @param value
	 *            the evaluation
	 * @return <code>true</code> if operation succeeds otherwise
	 *         <code>false</code>
	 */
	public boolean rateBook(BookPOJO book, UserPOJO user, int value);

	/**
	 * Allow the given {@link UserPOJO} to give back a book if he borrowed it.
	 * 
	 * @param user
	 *            the {@link UserPOJO} who give back the current {@link Book}.
	 * @param book
	 *            The {@link BookPOJO} to give back
	 * @return <code>true</code> if operation succeeds otherwise
	 *         <code>false</code>
	 */
	public boolean giveBack(BookPOJO book, UserPOJO user);

	/**
	 * Remove {@Link UserPOJO} from the queue
	 * 
	 * @param book
	 *            the {@link BookPOJO} borrowed
	 * @param user
	 *            the {@link UserPO} to remove
	 * @throws RemoteException
	 */
	public boolean removeFromQueue(BookPOJO book, UserPOJO user);

	/**
	 * Remove {@Link UserPOJO} from the queue
	 *
	 * @param user
	 *            the {@link UserPO} to remove
	 * @throws RemoteException
	 */
	public List<BookPOJO> hasBookInWait(UserPOJO user);

	/**
	 * Clear the whole database
	 */
	public boolean clear();
	
	/**
	 * Retrieves all {@link BookPOJO} that have been added <code>nbYear</code>
	 * back in past
	 * 
	 * @param nbYear
	 *            the number of year
	 * @param limit
	 *            the max {@link BookPOJO} to return
	 * @return a list of {@link BookPOJO}
	 */
	public List<BookPOJO> getBooksFromPastAndBorrowed(int nbYear, int limit);
	
	/**
	 * Add a comment
	 * @param book
	 * @param author
	 * @param rate
	 * @throws RemoteException
	 */
	public boolean addComment(BookPOJO book, String author, int rate);

}
