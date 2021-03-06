package fr.upem.rmirest.bilmancamp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.CommentImpl;
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

		try {
			return bTable.getBorrowedBooks(user);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

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

		try {
			return bTable.getBooksNotReturnedYet(userPOJO);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public List<BookPOJO> getPendingBooks(UserPOJO userPOJO) {

		try {
			return bTable.getPendingBooks(userPOJO);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

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
	public float getBookRate(BookPOJO book) {
		try {
			return (float) bTable.getRate(book.getId());
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return 0f;
	}

	@Override
	public int getBookRateNumber(BookPOJO book) {
		try {
			return bTable.getRateNumber(book);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return 0;
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
			boolean bDelete = bTable.delete();
			boolean uDelete = uTable.delete();
			boolean cDelete = cTable.delete();
			return bDelete && uDelete && cDelete;
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	@Override
	public List<BookPOJO> getBooksFromPastAndBorrowed(int nbYear, int limit) {

		try {
			return bTable.selectBookAddedAtLeast(getPreviousYears(nbYear), limit);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	/**
	 * Get Timestamp of date back in the past
	 * 
	 * @param nb
	 *            the number of year
	 * @return a {@link Timestamp}
	 */
	private static Timestamp getPreviousYears(int nb) {
		Calendar prevYears = Calendar.getInstance();
		Date current = new Date();
		prevYears.setTime(current);
		prevYears.add(Calendar.YEAR, -nb);
		return new Timestamp(current.getTime());
	}

	@Override
	public boolean addComment(BookPOJO book, String author, int rate, String content) {

		try {
			return bTable.insertComment(book, author, rate, content);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	@Override
	public List<CommentImpl> getComments(BookPOJO pojo) {
		try {
			return bTable.getComments(pojo);
		} catch (SQLException e) {
			Logger.getLogger(EmbeddedDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return Collections.emptyList();
	}
}
