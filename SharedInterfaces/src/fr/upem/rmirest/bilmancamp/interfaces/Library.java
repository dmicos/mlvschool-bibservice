package fr.upem.rmirest.bilmancamp.interfaces;

import java.util.List;

/**
 * Warning ! Server security : All Book's methods invoked here must not throw
 * any exceptions.
 *
 */
public interface Library {

	// Creations methods
	// TODO Image is a serializable interface.
	public void addBook(String title, List<String> authors, String summary, Image mainImage,
			List<Image> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException;

	public boolean addUser(String status, String firstname, String lastname, int cardNumber, String password)
			throws IllegalArgumentException;

	public User connect(String id, String password) throws IllegalArgumentException;

	// Researches
	public List<Book> searchBooks(String... keywords);

	// Categories
	public List<String> getCategories();

	public int getCategorySize();

	public List<Book> getCategoryBooks(String category);

	// Global book getters
	public List<Book> getMoreRecentBooks(int number);

	public List<Book> getBestRatedBooks(int number);

	public List<Book> getMostConsultedBooks(int number);

	public List<Book> getMostSimilarsBooks(Book book, int number);
}
