package fr.upem.rmirest.bilmancamp.database;

import java.util.List;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.User;

public interface Database {

	/**
	 * Add the given {@link Book} to the database.
	 * 
	 * @param book
	 *            the book to add.
	 */
	public void addBook(Book book);

	/**
	 * Add the given {@link User} to the database.
	 * 
	 * @param user
	 *            the user to add.
	 * @return <code>true</code> if the user was added correctly,
	 *         <code>false</code> otherwise.
	 */
	public boolean addUser(User user);

	/**
	 * Get a {@link User} object from the database values, using the given
	 * credentials.
	 * 
	 * @param id
	 *            the user id.
	 * @param password
	 *            the encrypted user password.
	 * @return the {@link User} or null if the credentials were invalids.
	 */
	public User connectUser(String id, String password);

	/**
	 * Get a list of {@link Book}s that corresponds to one of the keywords.
	 * Correspondences can be from tags, categories, or title part.
	 * 
	 * @param keywords
	 *            an {@link Iterable} of keywords.
	 * @return a list of books that corresponds to one of the keywords.
	 */
	public List<Book> searchBookFromKeywords(String... keywords);

	/**
	 * Get all the categories of books in the database.
	 * 
	 * @return a list of all the categories.
	 */
	public List<String> getCategories();

	/**
	 * Get all the {@link Book}s that are of the given category.
	 * 
	 * @param category
	 * @return a list of books of the given category.
	 */
	public List<Book> getBookFromCategory(String category);

	/**
	 * Get the <code>number</code> most recent {@link Book}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most recent books.
	 */
	public List<Book> getBookRecents(int number);

	/**
	 * Get the <code>number</code> best rated {@link Book}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> best rates books.
	 */
	public List<Book> getBookBestRate(int number);

	/**
	 * Get the <code>number</code> most consulted {@link Book}s.
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most consulted books.
	 */
	public List<Book> getBookMostConsulted(int number);

	/**
	 * Get the <code>number</code> books the most similar to the given one.
	 * 
	 * @param book
	 *            the comparaison book.
	 * @param number
	 *            the wanted number of books.
	 * @return a list of the <code>number</code> books the most similar to
	 *         <code>book</code>.
	 */
	public List<Book> getBookMostSimilar(Book book, int number);

}
