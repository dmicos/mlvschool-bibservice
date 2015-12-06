package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;
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
	 */
	public void addBook(String title, List<String> authors, String summary, Image mainImage,
			List<Image> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException;

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
	 */
	public boolean addUser(String status, String firstName, String lastName, int cardNumber, String password)
			throws IllegalArgumentException;

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
	 *             TODO
	 */
	public User connect(String id, String password) throws IllegalArgumentException;

	/**
	 * Search a {@link Book} using some keywords, which can be tags, categories,
	 * author, or part of the title.
	 * 
	 * @param keywords
	 *            the list of keywords used to search.
	 * @return a list of {@link Book} which corresponds to the keywords.
	 */
	public List<Book> searchBooks(String... keywords);

	/**
	 * Get the existing categories of books in the library.
	 * 
	 * @return the list of categories.
	 */
	public List<String> getCategories();

	/**
	 * TODO ?
	 * 
	 * @return
	 */
	public int getCategorySize();

	/**
	 * Get all the {@link Book}s which are of the given category.
	 * 
	 * @param category
	 *            the category of {@link Book}s which are researched.
	 * @return a list of {@link Book}s of the given category.
	 */
	public List<Book> getCategoryBooks(String category);

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most recently added books.
	 */
	public List<Book> getMoreRecentBooks(int number);

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> first books based of book's
	 *         evaluations.
	 */
	public List<Book> getBestRatedBooks(int number);

	/**
	 * 
	 * @param number
	 * @return a list of the <code>number</code> most borrowed books.
	 */
	public List<Book> getMostConsultedBooks(int number);

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
	public List<Book> getMostSimilarsBooks(Book book, int number);

	/**
	 * Try to borrow the given {@link Book}. If not available, then user will
	 * notify later once available
	 * 
	 * @param book
	 *            the {@link Book} to borrow
	 * @param user
	 *            the {@link User} borrowing the {@link Book}
	 * @return <code>true</code> if available otherwise <code>false</code>
	 */
	public boolean borrow(Book book, User user);

}
