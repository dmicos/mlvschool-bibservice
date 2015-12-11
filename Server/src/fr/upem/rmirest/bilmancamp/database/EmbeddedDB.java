package fr.upem.rmirest.bilmancamp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;
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
	public boolean addBook(BookPOJO book) {

		try {
			return bTable.insert(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(UserPOJO user) {

		try {
			return uTable.insert(user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean addUser(UserPOJO user, String password) {

		try {
			return uTable.insert(user, password);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	@Override
	public UserPOJO connectUser(String id, String password) {

		try {
			Optional<UserPOJO> op = uTable.exist(id, password);
			return op.isPresent() ? op.get() : null;
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<BookPOJO> searchBookFromKeywords(String... keywords) {

		try {
			return bTable.search(keywords);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public boolean addCategory(String category) {
		try {
			cTable.insert(category);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		return true;
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
	public List<BookPOJO> getBookFromCategory(String category) {
		return searchBookFromKeywords(category);
	}

	@Override
	public List<BookPOJO> getBookRecents(int number) {

		try {
			return bTable.selectMostRecent(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getBookBestRate(int number) {

		try {
			return bTable.selectMostRated(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getBookMostConsulted(int number) {

		try {
			return bTable.selectMostConsulted(number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getBookMostSimilar(BookPOJO book, int number) {

		try {
			return bTable.mostSimilar(book, number);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getBorrowedBooks(UserPOJO user) {
		// TODO I'm too afraid to touch this database. I'll probably break
		// something.
		return Collections.emptyList();
	}

	@Override
	public boolean borrow(BookPOJO book, UserPOJO user) {

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
	public List<BookPOJO> getBooks(UserPOJO userPOJO) {
		// TODO Auto-generated method stub for Yann the Database master
		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getPendingBooks(UserPOJO userPOJO) {
		// TODO Auto-generated method stub for Yann the Database master
		return Collections.emptyList();
	}

	@Override
	public List<UserPOJO> getQueue(BookPOJO book, int limit) {
		try {
			return bTable.getQueue(book, limit);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public boolean addToQueue(UserPOJO user, BookPOJO book) {
		try {
			return bTable.addToQueue(book, user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean isAvailable(BookPOJO book) {

		try {
			return bTable.canBorrow(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean rateBook(BookPOJO book, UserPOJO user, int value) {
		try {
			return bTable.rate(user, book, value);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean giveBack(BookPOJO book, UserPOJO user) {

		try {
			return bTable.giveBack(book, user);

		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean removeFromQueue(BookPOJO book, UserPOJO user) {

		try {
			return bTable.removeFromQueue(book, user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public List<BookPOJO> hasBookInWait(UserPOJO user) {

		try {
			return bTable.hasBookInWait(user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public boolean clear() {

		try {
			return bTable.delete() && uTable.delete();
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}
}
