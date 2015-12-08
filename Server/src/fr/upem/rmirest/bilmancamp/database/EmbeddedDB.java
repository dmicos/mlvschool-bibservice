package fr.upem.rmirest.bilmancamp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
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
	}

	@Override
	public boolean addBook(Book book) {

		try {
			return bTable.insert(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(User user) {

		try {
			return uTable.insert(user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(User user, String password) {

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
	public List<Book> searchBookFromKeywords(String... keywords) {

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
	public List<Book> getBookFromCategory(String category) {
		return searchBookFromKeywords(category);
	}

	@Override
	public List<Book> getBookRecents(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBookBestRate(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBookMostConsulted(int number) {

		try {
			return bTable.selectMostConsulted(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("EmbeddedDB Failed to perform query");
		}
	}

	@Override
	public List<Book> getBookMostSimilar(Book book, int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean borrow(Book book, User user) {

		try {
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
	public boolean addToQueue(User user, Book book) {
		try {
			return bTable.addToQueue(book, user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean isAvailable(Book book) {

		try {
			return bTable.canBorrow(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

}