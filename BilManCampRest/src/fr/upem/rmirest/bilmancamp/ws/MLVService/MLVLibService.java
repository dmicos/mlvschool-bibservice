package fr.upem.rmirest.bilmancamp.ws.MLVService;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.helpers.DBHelper;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;

public class MLVLibService {

	private final Database database;
	private final static int SELL_ALLOW_AFTER = 2;

	/**
	 * Use by default the embedded database
	 */
	public MLVLibService() {
		this.database = DBHelper.embeddedDB();
	}

	/**
	 * Returns the most consulted {@link Book}
	 * 
	 * @param number
	 *            the max rows to retrieve
	 * @return
	 */
	public BookPOJO[] getMostConsultedBook(int number) {
		return database.getBookMostConsulted(number).toArray(new BookPOJO[0]);
	}

	/**
	 * Returns the
	 * 
	 * @param number
	 *            the max rows to retrieve
	 * @return
	 */
	public BookPOJO[] getMostSimilarBook(BookPOJO model, int number) {
		return database.getBookMostSimilar(model, number).toArray(new BookPOJO[0]);

	}

	/**
	 * Search all {@link BookPOJO} that match given tags
	 * 
	 * @param tags
	 *            the tags
	 * @return an array of {@link BookPOJO}
	 */
	public BookPOJO[] searchBooks(String... tags) {
		return database.searchBookFromKeywords(tags).toArray(new BookPOJO[0]);
	}

	/**
	 * Buy a book by contacting checking web services
	 * 
	 * @param book
	 * @param devise
	 * @param cardNumber
	 * @return
	 */
	public boolean buy(BookPOJO book, String devise, long cardNumber) {

		if (!database.isAvailable(book)) {
			throw new IllegalArgumentException("The book is not available");
		}

		// Todo:Contact Bank
		// todo:decrease book table

		return false;
	}

	/**
	 * Get all the categories of books in the database.
	 * 
	 * @return an array of all the categories.
	 */
	public String[] getCategories() {
		return database.getCategories().toArray(new String[0]);
	}

	/**
	 * Get all the {@link BookPOJO}s that are of the given category.
	 * 
	 * @param category
	 * @return an array of books of the given category.
	 */
	public BookPOJO[] getBookFromCategory(String category) {
		return database.getBookFromCategory(category).toArray(new BookPOJO[0]);
	}

	/**
	 * Get all books available from sell
	 * 
	 * @param limit
	 *            the max book to return
	 * @return an array of {@link BookPOJO}
	 */
	public BookPOJO[] getBookForSell(int limit) {
		return database.getBooksFromPastAndBorrowed(SELL_ALLOW_AFTER, limit).toArray(new BookPOJO[0]);
	}
}
