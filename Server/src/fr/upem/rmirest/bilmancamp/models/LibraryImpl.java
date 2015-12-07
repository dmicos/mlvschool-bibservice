package fr.upem.rmirest.bilmancamp.models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LibraryImpl extends UnicastRemoteObject implements Library {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7781484523332395659L;
	// Library values
	private final Database database;

	public LibraryImpl(Database database) throws RemoteException {
		super();
		this.database = database;
	}

	@Override
	public void addBook(String title, List<String> authors, String summary, Image mainImage,
			List<Image> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException {

		Book newBook = new BookImpl(title, authors, summary, categories, price, tags, mainImage, secondaryImages);
		database.addBook(newBook);
	}

	@Override
	public boolean addUser(String status, String firstName, String lastName, int cardNumber, String password)
			throws IllegalArgumentException {

		User newUser = new UserImpl(status, firstName, lastName, password, cardNumber, new ArrayList<>());
		return database.addUser(newUser, password);
	}

	@Override
	public User connect(String id, String password) throws IllegalArgumentException {
		User user = database.connectUser(id, password);
		if (user == null) {
			throw new IllegalArgumentException("Invalid authentification for user " + id);
		}
		return user;
	}

	@Override
	public List<Book> searchBooks(String... keywords) {
		return database.searchBookFromKeywords(keywords);
	}

	@Override
	public List<String> getCategories() {
		return Collections.unmodifiableList(database.getCategories());
	}

	@Override
	public int getCategorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Book> getCategoryBooks(String category) {
		return database.getBookFromCategory(category);
	}

	@Override
	public List<Book> getMoreRecentBooks(int number) {
		return database.getBookRecents(number);
	}

	@Override
	public List<Book> getBestRatedBooks(int number) {
		return database.getBookBestRate(number);
	}

	@Override
	public List<Book> getMostConsultedBooks(int number) {
		return database.getBookMostConsulted(number);
	}

	@Override
	public List<Book> getMostSimilarsBooks(Book book, int number) {
		return database.getBookMostSimilar(book, number);
	}
	
	/* Actions on the content */

	@Override
	public boolean borrow(Book book, User user) {

		if (database.isAvailable(book)) {
			return database.borrow(book, user);
		}

		database.addToQueue(user, book);
		return false;
	}

	@Override
	public void giveBack(Book book, User user) throws RemoteException {
		// TODO : Need to add some methods to the Database interface.
		throw new NotImplementedException();
	}

	@Override
	public boolean rateBook(Book book, User user, int value) {
		// TODO : This one probably needs some improvement of the Database
		// interface.
		throw new NotImplementedException();
	}

}
