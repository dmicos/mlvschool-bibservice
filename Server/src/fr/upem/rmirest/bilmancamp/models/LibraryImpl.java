package fr.upem.rmirest.bilmancamp.models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import utils.Mapper;

public class LibraryImpl extends UnicastRemoteObject implements Library {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7781484523332395659L;
	// Library values
	private final Database database;
	private final Map<User, MailBox<Book>> addresses;

	public LibraryImpl(Database database) throws RemoteException {
		super();
		this.database = database;
		addresses = new ConcurrentHashMap<>();

	}

	@Override
	public void addBook(String title, List<String> authors, String summary, Image mainImage,
			List<Image> secondaryImages, List<String> categories, double price, List<String> tags)
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
		return database.searchBookFromKeywords(keywords).stream().map(b -> new BookImpl(b))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getCategories() throws RemoteException {
		return Collections.unmodifiableList(database.getCategories());
	}

	@Override
	public int getCategorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Book> getCategoryBooks(String category) throws RemoteException {
		return database.getBookFromCategory(category).stream().map(b -> new BookImpl(b)).collect(Collectors.toList());
	}

	@Override
	public List<Book> getMoreRecentBooks(int number) throws RemoteException {
		return database.getBookRecents(number).stream().map(b -> new BookImpl(b)).collect(Collectors.toList());
	}

	@Override
	public List<Book> getBestRatedBooks(int number) throws RemoteException {
		return database.getBookBestRate(number).stream().map(b -> new BookImpl(b)).collect(Collectors.toList());
	}

	@Override
	public List<Book> getMostConsultedBooks(int number) throws RemoteException {
		return database.getBookMostConsulted(number).stream().map(b -> new BookImpl(b)).collect(Collectors.toList());
	}

	@Override
	public List<Book> getMostSimilarsBooks(Book book, int number) throws RemoteException {

		return database.getBookMostSimilar(Mapper.createBookPOJO(book), number).stream().map(b -> new BookImpl(b))
				.collect(Collectors.toList());
	}

	/* Actions on the content */

	@Override
	public boolean borrow(Book book, User user) throws RemoteException {
		return database.borrow(Mapper.createBookPOJO(book), Mapper.createUserPOJO(user));
	}

	@Override
	public boolean rateBook(Book book, User user, int value) throws RemoteException {
		return database.rateBook(Mapper.createBookPOJO(book), Mapper.createUserPOJO(user), value);
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

	@Override
	public void disconnect(User user) throws RemoteException {
		addresses.remove(user);
		user.disconnect();
	}
}
