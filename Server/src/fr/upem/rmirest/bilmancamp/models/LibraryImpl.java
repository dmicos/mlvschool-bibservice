package fr.upem.rmirest.bilmancamp.models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.BookComment;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.modelloaders.LibraryImplDataLoader;
import fr.upem.rmirest.bilmancamp.modelloaders.RateWrapper;
import utils.Mapper;

public class LibraryImpl extends UnicastRemoteObject implements Library {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	// Library values
	private final Database database;
	private final Map<User, MailBox<Book>> addresses;
	private final Map<String, String> categories;
	private final List<String> status;

	private LibraryImpl(Database database) throws RemoteException {
		super(0);
		this.database = database;
		addresses = new ConcurrentHashMap<>();
		categories = new HashMap<>();
		status = new ArrayList<>();
	}

	public static Library createLibraryImpl(Database database, String configPath)
			throws JsonMappingException, IOException {
		LibraryImpl lib = new LibraryImpl(database);
		// Initialize the loader and load data into the fields.
		LibraryImplDataLoader loader = LibraryImplDataLoader.createLoader(configPath);
		lib.categories.putAll(loader.getCategories());
		lib.status.addAll(loader.getStatus());
		// Return the fully initialized library.
		return lib;
	}

	/**
	 * Populates the database of the current {@link LibraryImpl} with some
	 * values from its own category. Will also parse the given json files which
	 * must contains data for users and books.
	 * 
	 * @param userFilePath
	 * @param bookFilePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JsonProcessingException
	 */
	public void populateDatabase(String userFilePath, String bookFilePath, String rateFilePath)
			throws JsonProcessingException, FileNotFoundException, IOException {
		// Add categories to the database
		categories.forEach((key, value) -> database.addCategory(key));
		// Load some users
		List<UserPOJO> users = LibraryImplDataLoader.loadUsers(userFilePath);
		users.forEach(user -> database.addUser(user));
		// Load some books
		List<BookPOJO> books = LibraryImplDataLoader.loadBooks(bookFilePath);
		books.forEach(book -> database.addBook(book));
		// Get some rates on the books
		List<RateWrapper> rates = LibraryImplDataLoader.loadRates(rateFilePath);
		rates.forEach(rate -> database.rateBook(rate.getBook(), rate.getUser(), rate.getValue()));

	}

	@Override
	public void addBook(String title, List<String> authors, String summary, String mainImage,
			List<String> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException, RemoteException {

		database.addBook(new BookPOJO(0, title, authors, summary, categories, price, tags, mainImage, secondaryImages));
	}

	@Override
	public boolean addUser(String status, String firstName, String lastName, int cardNumber, String password)
			throws IllegalArgumentException, RemoteException {

		return database.addUser(new UserPOJO(0, status, firstName, lastName, password, cardNumber, new ArrayList<>()),
				password);
	}

	@Override
	public User connect(String id, String password) throws IllegalArgumentException, RemoteException {
		UserPOJO model = database.connectUser(id, password);
		if (model == null) {
			throw new IllegalArgumentException("Invalid authentification for user " + id);
		}
		return new UserImpl(model);
	}

	@Override
	public List<Book> searchBooks(String... keywords) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.searchBookFromKeywords(keywords));
	}

	@Override
	public List<String> getCategories() throws RemoteException {
		return Collections.unmodifiableList(database.getCategories());
	}

	@Override
	public String getCategoryDescription(String category) {
		return categories.get(category);
	}

	@Override
	public List<String> getStatus() throws RemoteException {
		return Collections.unmodifiableList(status);
	}

	@Override
	public List<Book> getCategoryBooks(String category) throws RemoteException {
		List<BookPOJO> books = database.getBookFromCategory(category);
		return Pojos.booksPojoToBooksRemote(books);
	}

	@Override
	public List<Book> getMoreRecentBooks(int number) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getBookRecents(number));
	}

	@Override
	public List<Book> getBestRatedBooks(int number) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getBookBestRate(number));
	}

	@Override
	public List<Book> getMostConsultedBooks(int number) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getBookMostConsulted(number));
	}

	@Override
	public List<Book> getMostSimilarsBooks(Book book, int number) throws RemoteException {

		return Pojos.booksPojoToBooksRemote(database.getBookMostSimilar(Mapper.createBookPOJO(book), number));
	}

	/* Actions on the content */

	@Override
	public List<Book> getPendingBooks(User user) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getPendingBooks(Mapper.createUserPOJO(user)));
	}

	@Override
	public boolean borrow(Book book, User user) throws RemoteException {
		return database.borrow(Mapper.createBookPOJO(book), Mapper.createUserPOJO(user));
	}

	@Override
	public boolean cancelRegistration(Book book, User user) throws RemoteException {
		return database.removeFromQueue(Mapper.createBookPOJO(book), Mapper.createUserPOJO(user));
	}

	@Override
	public boolean rateBook(Book book, User user, int value) throws RemoteException {
		return database.rateBook(Mapper.createBookPOJO(book), Mapper.createUserPOJO(user), value);
	}

	@Override
	public float getRate(Book book) throws RemoteException {
		return database.getBookRate(Mapper.createBookPOJO(book));
	}

	@Override
	public int getRateNumber(Book book) throws RemoteException {
		return database.getBookRateNumber(Mapper.createBookPOJO(book));
	}

	@Override
	public User connect(String id, String password, MailBox<Book> callback) throws RemoteException {

		Objects.requireNonNull(callback);

		// database authentication ?
		UserPOJO uPojo = database.connectUser(id, password);

		// authentication OK
		if (uPojo != null) {

			User user = new UserImpl(uPojo);
			addresses.put(user, callback);

			// Notification
			List<BookPOJO> books = database.hasBookInWait(uPojo);

			// For each available book
			for (BookPOJO b : books) {
				contactIfOnline(uPojo, new BookImpl(b), callback);
			}

			return user;
		}

		return null;
	}

	@Override
	public boolean giveBack(Book book, User user) throws RemoteException {

		BookPOJO bPojo = Mapper.createBookPOJO(book);
		UserPOJO uPojo = Mapper.createUserPOJO(user);

		if (database.giveBack(bPojo, uPojo)) {

			List<UserPOJO> queue = database.getQueue(bPojo, 1);

			if (!queue.isEmpty()) {
				UserPOJO head = queue.get(0);
				contactIfOnline(head, book, addresses.get(new UserImpl(head)));
			}

			return true;
		}

		return false;
	}

	@Override
	public List<Book> getBooks(User user) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getBooks(Mapper.createUserPOJO(user)));
	}

	@Override
	public List<Book> getBookHistory(User user) throws RemoteException {
		return Pojos.booksPojoToBooksRemote(database.getBorrowedBooks(Mapper.createUserPOJO(user)));
	}

	@Override
	public boolean addComment(Book book, String author, int rate, String content) throws RemoteException {

		return database.addComment(Mapper.createBookPOJO(book), author, rate, content);
	}

	@Override
	public List<BookComment> getComment(Book book) throws RemoteException {
		return database.getComments(Mapper.createBookPOJO(book)).stream()
				.map(com -> new CommentImpl(com.getAuthors(), com.getContent())).collect(Collectors.toList());
	}

	@Override
	public void disconnect(User user) throws RemoteException {
		addresses.remove(user);
		user.disconnect();
	}

	/**
	 * Handle {@link Book} availability of a book by notifying user
	 * 
	 * @param uPojo
	 *            The {@link UserPOJO} to notify
	 * @param book
	 *            The {@link Book} to borrow
	 * @throws RemoteException
	 */
	private void contactIfOnline(UserPOJO uPojo, Book book, MailBox<Book> callbackAddr) throws RemoteException {

		if (callbackAddr != null) {

			BookPOJO bPojo = Mapper.createBookPOJO(book);
			callbackAddr.receive(book);
			database.borrow(bPojo, uPojo);
			database.removeFromQueue(bPojo, uPojo);
		}
	}
}
