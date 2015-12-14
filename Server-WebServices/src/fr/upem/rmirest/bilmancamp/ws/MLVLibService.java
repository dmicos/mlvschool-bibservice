package fr.upem.rmirest.bilmancamp.ws;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.helpers.DBHelper;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.ws.selling.SellingService;
import fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceServiceLocator;

public class MLVLibService {

	private static Database database;
	private static SellingService sellingService;
	public static int MAX_LIMIT = 1000;

	/**
	 * Use by default the embedded database
	 * 
	 * @throws ServiceException
	 */
	public MLVLibService() throws ServiceException {

		database = DBHelper.embeddedDB();
		sellingService = new SellingServiceServiceLocator().getSellingService();
	}

	/**
	 * Returns the most consulted {@link Book}
	 * 
	 * @param number
	 *            the max rows to retrieve
	 * @return
	 */
	public BookPOJO getMostConsultedBook(int number) {
		return database.getBookMostConsulted(number).toArray(new BookPOJO[0])[0];
	}

	public String[] getMostConsultedBookFormatted(int number) {
		return MLVLibService.fomatBook(database.getBookMostConsulted(number).toArray(new BookPOJO[0]));
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

	public String[] searchFormattedBooks(String... tags) {
		return MLVLibService.fomatBook(database.searchBookFromKeywords(tags).toArray(new BookPOJO[0]));
	}

	public boolean sellBook(long cardID, String secret, String devise, double price) throws RemoteException {

		return sellingService.sellBook(cardID, secret, price, devise);
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

	public String[] getFormattedBookFromCategory(String category) {
		return MLVLibService.fomatBook(database.getBookFromCategory(category).toArray(new BookPOJO[0]));
	}

	public BookPOJO[] getBooksForSell(int limit) {
		return database.getBooksFromPastAndBorrowed(2, limit).toArray(new BookPOJO[0]);
	}

	/**
	 * Returns available books for sell
	 * 
	 * @return
	 */
	public int getTotalBooks() {
		return getBooksForSell(MAX_LIMIT).length;
	}

	/**
	 * Get a range a books to sell
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public BookPOJO[] getRange(int offset, int limit) {

		List<BookPOJO> data = database.getBooksFromPastAndBorrowed(2, MAX_LIMIT);
		int count = data.size();

		if (offset >= count)
			throw new IllegalArgumentException("Invalid offset");

		int top = (offset + limit) < count ? offset + limit : count;

		return data.subList(offset, top).toArray(new BookPOJO[0]);
	}

	/**
	 * Get formatted Book
	 * @param limit
	 * @return
	 */
	public String[] getFormattedBooks(int limit) {

		return MLVLibService.fomatBook(getBooksForSell(limit));
	}

	/**
	 * Get a range of formatted books
	 * @param limit
	 * @param offset
	 * @return
	 */
	public String[] getFormattedRange(int offset, int limit) {

		return MLVLibService.fomatBook(getRange(offset, limit));
	}

	/**
	 * Get an image of given book
	 * @param idBook
	 * @return
	 */
	public String getImage(int idBook){
	
		for(BookPOJO tmp : getBooksForSell(MAX_LIMIT)){
			
			if(tmp.getId() == idBook)
				return tmp.getMainImage();
		}
		
		return null;
	}
	
	private static String[] fomatBook(BookPOJO[] val) {

		List<String> list = new ArrayList<>();

		for (BookPOJO b : val) {
			List<String> bVal = Arrays.asList(b.getId() + "", b.getTitle(), b.getCategories()[0], b.getAuthors()[0],
					b.getPrice() + "", b.getRateNumber() + "");
			list.addAll(bVal);
		}
		return list.toArray(new String[0]);

	}

}
