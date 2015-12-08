package fr.upem.rmirest.bilmancamp.database;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.persistence.BookTable;
import fr.upem.rmirest.bilmancamp.persistence.CategoryTable;
import fr.upem.rmirest.bilmancamp.persistence.UserTable;

/**
 * 
 * @author ybilissor
 *
 */
public class EmbeddedDB implements Database {

	private final BookTable bTable;
	private final UserTable uTable;
	private final CategoryTable cTable;
	private final Map<User, MailBox<Book>> addresses;

	/**
	 * Create an embedded database
	 * 
	 * @param conn
	 *            The {@link Connection} to the database
	 */
	public EmbeddedDB(Connection conn) {
		bTable = new BookTable(conn);
		uTable = new UserTable(conn);
		cTable = new CategoryTable(conn);
		addresses = new HashMap<>();

	}

	@Override
	public boolean addBook(Book book) throws RemoteException {

		try {
			return bTable.insert(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(User user) throws RemoteException {

		try {
			return uTable.insert(user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(User user, String password) throws RemoteException {

		try {
			return uTable.insert(user, password);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	@Override
	public User connectUser(String id, String password) {

		try {
			Optional<User> op = uTable.exist(id, password);
			return op.isPresent() ? op.get() : null;
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Book> searchBookFromKeywords(String... keywords) throws RemoteException {

		try {
			return bTable.search(keywords);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<String> getCategories() {

		try {
			return cTable.select();
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<Book> getBookFromCategory(String category) throws RemoteException {
		return searchBookFromKeywords(category);
	}

	@Override
	public List<Book> getBookRecents(int number) throws RemoteException {

		try {
			return bTable.selectMostRecent(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<Book> getBookBestRate(int number) throws RemoteException {

		try {
			return bTable.selectMostRated(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<Book> getBookMostConsulted(int number) throws RemoteException {

		try {
			return bTable.selectMostConsulted(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<Book> getBookMostSimilar(Book book, int number) throws RemoteException {

		try {
			return bTable.mostSimilar(book, number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public boolean borrow(Book book, User user) throws RemoteException {

		try {

			if (bTable.hasAlreadyBorrowed(book, user))
				return false;

			// Book is not available. Add in queue if not
			if (!isAvailable(book)) {

				if (!bTable.isAlreadyInQueue(book, user))
					addToQueue(user, book);

				return false;
			}

			return bTable.borrow(book, user);

		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public List<User> getQueue(Book book, int limit) {
		try {
			return bTable.getQueue(book, limit);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public boolean addToQueue(User user, Book book) throws RemoteException {
		try {
			return bTable.addToQueue(book, user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean isAvailable(Book book) throws RemoteException {

		try {
			return bTable.canBorrow(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean rateBook(Book book, User user, int value) throws RemoteException {
		try {
			return bTable.rate(user, book, value);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public User connectUser(String id, String password, MailBox<Book> callback) {
		Objects.requireNonNull(callback);

		User user = connectUser(id, password);

		if (user != null)
			addresses.put(user, callback);

		return user;
	}

	@Override
	public boolean giveBack(Book book, User user) throws RemoteException {

		try {
			if (bTable.giveBack(book, user)) {

				// check if there is some people in the queue
				List<User> queue = bTable.getQueue(book, 1);

				// Has waiters
				if (!queue.isEmpty()) {
					// Notify user
					MailBox<Book> callbackAddr = addresses.get(queue.get(0));
					if (callbackAddr != null) {
						callbackAddr.receive(book);
						borrow(book, queue.get(0));
						
						bTable.removeFromQueue(book, queue.get(0));
					}
				}

				return true;
			}

		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}
}
