package fr.upem.rmirest.bilmancamp.persistence;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.CommentImpl;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class BookTable extends AbstractTableModel<BookPOJO> {

	public BookTable(Connection connection) {
		super(connection);
	}

	@Override
	public boolean insert(BookPOJO book) throws SQLException {

		Objects.requireNonNull(book);

		// Prepare SQL Query in order to avoid SQL Injection
		PreparedStatement ps = getConnection().prepareStatement(
				"INSERT INTO book(title,description,price,tags,viewCounter,authors,datetime,catName,image,ptags,stock) VALUES(?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		// fill the query blank
		ps.setString(1, book.getTitle());
		ps.setString(2, book.getSummary());
		ps.setDouble(3, book.getPrice());
		ps.setString(4, String.join(",", book.getTags()));
		ps.setDouble(5, book.getConsultationNumber());
		ps.setString(6, String.join(",", book.getAuthors()));
		ps.setString(7, String.join(",", book.getCategories()));
		ps.setString(8,
				String.join(",",
						Stream.concat(Stream.of(book.getMainImage()), Arrays.asList(book.getSecondaryImages()).stream())
								.collect(Collectors.toList())));
		ps.setString(9, String.join(",", createKeyWords(book)));
		ps.setInt(10, 1);

		return ps.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Object... pk) throws SQLException {

		if (pk.length != 1)
			throw new IllegalArgumentException("Only one key is expected");

		PreparedStatement ps = getConnection().prepareStatement("DELETE FROM book WHERE id = ?");
		ps.setObject(1, pk[0]);

		return ps.executeUpdate() > 0;
	}

	@Override
	public List<BookPOJO> select() throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		Statement statement = getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM book ORDER BY title asc");
		extractFromResultSet(content, rs);
		consult(content);
		return content;
	}

	@Override
	public List<BookPOJO> select(int offset, int limit) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM book   ORDER BY title asc LIMIT ? OFFSET ?");
		ps.setInt(1, limit);
		ps.setInt(1, offset);
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	/**
	 * Select most recent book
	 * 
	 * @param limit
	 *            the max result
	 * @return the list of {@link Book}
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public List<BookPOJO> selectMostRecent(int limit) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM book ORDER BY datetime desc LIMIT ? ");
		ps.setInt(1, limit);
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	/**
	 * Rate a {@link Book} by given {@link User}
	 * 
	 * @param user
	 *            the user who wants to rate
	 * @param book
	 *            the book to rate
	 * @param value
	 *            the value
	 * @return
	 * @throws RemoteException
	 */
	public boolean rate(UserPOJO user, BookPOJO book, int value) throws SQLException {

		Objects.requireNonNull(user);
		Objects.requireNonNull(book);

		if (value < 0)
			return false;

		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO rate(idUser,idBook,value) VALUES(?,?,?)");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());
		ps.setInt(3, value);

		return ps.executeUpdate() > 0;

	}

	/**
	 * Return the most rated book
	 * 
	 * @param limit
	 *            the page size
	 * @return a list of the <code>limit</code> most rated
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public List<BookPOJO> selectMostRated(int limit) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM book INNER JOIN  (select idBook ,AVG(VALUE) as rank from rate r  group by idBook) ON idBook = id order by rank desc LIMIT ?");
		ps.setInt(1, limit);
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		System.out.println(content);
		return content;
	}

	@Override
	public Optional<BookPOJO> find(Object... pk) throws SQLException {

		if (pk.length != 1)
			throw new IllegalArgumentException("Only one key is expected");

		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM book WHERE id=?");
		ps.setObject(1, pk[0]);
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			Optional<BookPOJO> op = Optional.of(extractRow(rs));
			consult(Arrays.asList(op.get()));
			return op;
		}
		return Optional.empty();
	}

	@Override
	public List<BookPOJO> search(String... tags) throws SQLException {

		if (tags.length < 1)
			throw new IllegalArgumentException("You must give at least one keyword");

		List<String> lowTags = Arrays.asList(tags);
		lowTags.replaceAll(item -> "'%".concat(item.toLowerCase()).concat("%'"));

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM book WHERE LCASE(ptags) LIKE ")
				.append(String.join(" OR LCASE(ptags) LIKE ", lowTags)).append(" ORDER BY title");

		List<BookPOJO> content = new ArrayList<>();
		extractFromResultSet(content, getConnection().createStatement().executeQuery(builder.toString()));
		consult(content);
		return content;
	}

	@Override
	public boolean update(BookPOJO oldVal, BookPOJO newVal) throws SQLException {

		Objects.requireNonNull(oldVal);
		Objects.requireNonNull(newVal);

		PreparedStatement ps = getConnection().prepareStatement(
				"UPDATE book SET title=?,description=?,price=?,tags=?,authors=?,viewCounter=?,catName=?,image=?,ptags=? WHERE id=?");
		ps.setString(1, newVal.getTitle());
		ps.setString(2, newVal.getSummary());
		ps.setDouble(3, newVal.getPrice());
		ps.setString(4, String.join(",", newVal.getTags()));
		ps.setString(5, String.join(",", newVal.getAuthors()));
		ps.setInt(6, newVal.getConsultationNumber());
		ps.setString(7, String.join(",", newVal.getCategories()));
		ps.setString(8,
				String.join(",", Stream
						.concat(Stream.of(newVal.getMainImage()), Arrays.asList(newVal.getSecondaryImages()).stream())
						.collect(Collectors.toList())));
		ps.setString(9, String.join(",", createKeyWords(newVal)));
		ps.setInt(10, newVal.getId());

		return ps.executeUpdate() > 0;
	}

	/**
	 * Give back given {@link Book}
	 * 
	 * @param book
	 *            The {@link Book} to gibe back
	 * @param user
	 *            the borrower
	 * @return <code>true</code> if operation succeeds otherwise
	 *         <code>false</code>
	 * @throws SQLException
	 * @throws RemoteException
	 */

	public boolean giveBack(BookPOJO book, UserPOJO user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		// maintain stock
		updateStock(book, 1);

		// Keep track
		PreparedStatement ps = getConnection()
				.prepareStatement("UPDATE borrow SET state=1 WHERE idBook=? and idUser=? ");
		ps.setInt(1, book.getId());
		ps.setInt(2, user.getId());

		return ps.executeUpdate() > 0;
	}

	private void updateStock(BookPOJO book, int value) throws SQLException {
		PreparedStatement ps1 = getConnection().prepareStatement("UPDATE book SET stock = stock + ? WHERE id=?");
		ps1.setInt(1, value);
		ps1.setInt(2, book.getId());
		ps1.executeUpdate();
	}

	/**
	 * Borrow given {@link Book}
	 * 
	 * @param book
	 *            The {@link Book} to borrow
	 * @param user
	 *            the borrower
	 * @return <code>true</code> if operation succeeds otherwise
	 *         <code>false</code>
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public boolean borrow(BookPOJO book, UserPOJO user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		// decrease stock
		updateStock(book, -1);

		// Insert operation
		PreparedStatement ps = getConnection()
				.prepareStatement("INSERT INTO borrow(idUser,idBook,datetime,state) VALUES(?,?,CURRENT_TIMESTAMP,0)");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());

		return ps.executeUpdate() > 0;
	}

	/**
	 * Get the <code>limit</code> {@link User} awaiting for given {@link Book}
	 * 
	 * @param book
	 *            the {@link Book}
	 * @param limit
	 *            the limit of result
	 * @return
	 */
	public List<UserPOJO> getQueue(BookPOJO book, int limit) throws SQLException {

		Objects.requireNonNull(book);

		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM queue q INNER JOIN user u ON q.idUser = u.id ORDER BY position asc LIMIT ?");
		ps.setInt(1, limit);

		return UserTable.extractUserFromResultSet(ps.executeQuery());
	}

	/**
	 * Check if there are some {@link BookPOJO} in wait of borrowing
	 * 
	 * @param user
	 *            the borrower
	 * @return a list of {@link BookPOJO} that can be borrowed
	 * @throws SQLException
	 */
	public List<BookPOJO> hasBookInWait(UserPOJO user) throws SQLException {

		Objects.requireNonNull(user);

		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM queue q INNER JOIN book b ON q.idBook = b.id WHERE q.idUser=? ORDER BY position asc");
		ps.setInt(1, user.getId());

		List<BookPOJO> booksInQueue = new ArrayList<>();
		List<BookPOJO> availableBook = new ArrayList<>();

		extractFromResultSet(booksInQueue, ps.executeQuery());

		for (BookPOJO b : booksInQueue) {

			if (getQueue(b, 1).contains(user) && canBorrow(b)) {
				availableBook.add(b);
			}
		}

		return availableBook;
	}

	public List<BookPOJO> getBooksNotReturnedYet(UserPOJO user) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM book b WHERE b.id IN (SELECT idBook FROM borrow WHERE idUser=? AND state=0) ORDER BY title ASC");
		ps.setInt(1, user.getId());
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	public List<BookPOJO> getBorrowedBooks(UserPOJO user) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM book b WHERE b.id IN (SELECT idBook FROM borrow WHERE idUser=?) ORDER BY title ASC");
		ps.setInt(1, user.getId());
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	public List<BookPOJO> getPendingBooks(UserPOJO user) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM book b WHERE b.id IN (SELECT idBook FROM queue WHERE idUser=? ) ORDER BY title ASC");
		ps.setInt(1, user.getId());
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	/**
	 * Add {@Link User} to the queue
	 * 
	 * @param book
	 *            the {@link Book} to borrow
	 * @param user
	 *            the {@link User} who wants to borrow
	 * @throws RemoteException
	 */
	public boolean addToQueue(BookPOJO book, UserPOJO user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO queue(idUser,idBook) Values(?,?)");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());
		return ps.executeUpdate() > 0;

	}

	/**
	 * Check if a user is already in the queue
	 * 
	 * @param book
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public boolean isAlreadyInQueue(BookPOJO book, UserPOJO user) throws SQLException {

		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM queue WHERE idUser=? AND idBook=?");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());

		return ps.executeQuery().first();
	}

	/**
	 * Check if he is attempting to borrow a book that he has already borrowed
	 * and not yet returned
	 * 
	 * @param book
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public boolean hasAlreadyBorrowed(BookPOJO book, UserPOJO user) throws SQLException {

		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM borrow WHERE idUser=? AND idBook=? AND State=0");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());

		return ps.executeQuery().first();
	}

	/**
	 * Remove {@Link UserPOJO} from the queue
	 * 
	 * @param book
	 *            the {@link BookPOJO} borrowed
	 * @param user
	 *            the {@link UserPO} to remove
	 * @throws RemoteException
	 */
	public boolean removeFromQueue(BookPOJO book, UserPOJO user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		PreparedStatement ps = getConnection().prepareStatement("DELETE FROM queue WHERE idUser=? AND idBook=?");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());
		return ps.executeUpdate() > 0;

	}

	/**
	 * Check if given book is available
	 * 
	 * @param book
	 * @return
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public boolean canBorrow(BookPOJO book) throws SQLException {

		PreparedStatement ps = getConnection().prepareStatement("SELECT stock FROM book WHERE id=?");
		ps.setObject(1, book.getId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			return rs.getInt("stock") > 0;
		}

		return false;
	}

	/**
	 * Retrieves the most consulted books
	 * 
	 * @param number
	 *            the page size
	 * @return a list {@link Book}
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public List<BookPOJO> selectMostConsulted(int number) throws SQLException {

		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM book ORDER BY viewCounter desc  LIMIT ? ");
		ps.setInt(1, number);
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}

	/***
	 * Create a list of key words from given {@link Book}
	 * 
	 * @param book
	 *            The {@link}
	 * @return a list of key words
	 * @throws RemoteException
	 */
	private List<String> createKeyWords(BookPOJO book) {

		List<String> kw = new ArrayList<>();
		kw.addAll(Arrays.asList(book.getTitle().split(" ")));
		kw.addAll(Arrays.asList(book.getCategories()));
		kw.addAll(Arrays.asList(book.getTags()));
		return kw;
	}

	/**
	 * 
	 * @param book
	 * @param limit
	 * @return
	 * @throws RemoteException
	 */
	public List<BookPOJO> mostSimilar(BookPOJO book, int limit) throws SQLException {

		Map<BookPOJO, Long> occurences = new HashMap<>();
		Set<String> kw = new HashSet<>();
		kw.addAll(createKeyWords(book));

		for (String item : kw) {

			search(item).stream().filter(b -> !b.equals(book))
					.collect(Collectors.groupingBy((BookPOJO b) -> b, Collectors.counting())).forEach((k, v) -> {
						occurences.merge(k, v, (a, b) -> b + v);
					});

		}

		return occurences.entrySet().stream().sorted((a, b) -> (int) (a.getValue() - b.getValue())).map(E -> E.getKey())
				.limit(limit).collect(Collectors.toList());
	}

	/**
	 * Increase the number of consultations
	 * 
	 * @param books
	 *            the list consulted {@link Book}
	 * @throws RemoteException
	 */
	private void consult(List<BookPOJO> books) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement("UPDATE book Set viewCounter=viewCounter+1 WHERE id=?");

		for (BookPOJO b : books) {
			ps.setInt(1, b.getId());
			ps.executeUpdate();
		}
	}

	/**
	 * Extract raw data from given {@link ResultSet}
	 * 
	 * @param dest
	 * @param rs
	 * @throws SQLException
	 */
	private void extractFromResultSet(List<BookPOJO> dest, ResultSet rs) throws SQLException {
		rs.beforeFirst();
		while (rs.next()) {
			dest.add(extractRow(rs));
		}
	}

	/**
	 * Extract a single row from given {@link ResultSet}
	 * 
	 * @param rs
	 *            an iterator on rows
	 * @return {@link Book}
	 * @throws SQLException
	 */
	private BookPOJO extractRow(ResultSet rs) throws SQLException {

		// Load all stored images
		List<String> images = Arrays.asList(rs.getString("image").split(",")).stream().collect(Collectors.toList());

		// Check and get secondaries if stored
		List<String> secondaries = images.size() > 1 ? images.subList(1, images.size()) : Collections.emptyList();

		// Create the book
		return new BookPOJO(rs.getInt("id"), rs.getString("title"), Arrays.asList(rs.getString("authors").split(",")),
				rs.getString("description"), Arrays.asList(rs.getString("catName").split(",")), rs.getDouble("price"),
				Arrays.asList(rs.getString("tags").split(",")), images.get(0), secondaries);

	}

	@Override
	public boolean delete() throws SQLException {
		Statement st = getConnection().createStatement();
		return (st.executeUpdate("DELETE FROM borrow") | st.executeUpdate("DELETE FROM rate")
				| st.executeUpdate("DELETE FROM queue") | st.executeUpdate("DELETE FROM book")) > 0;

	}
	

	public List<BookPOJO> selectBookAddedAtLeast(Timestamp previousYears, int limit) throws SQLException {
		
		List<BookPOJO> content = new ArrayList<>();
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM book b WHERE datetime > ? AND b.id IN(SELECT idBook FROM borrow) ORDER BY title LIMIT ?");
		ps.setTimestamp(1, previousYears);
		ps.setInt(2,limit);
		extractFromResultSet(content, ps.executeQuery());
		consult(content);
		return content;
	}
	
	/**
	 *  Add a commend
	 * @param book
	 * @param author
	 * @param rate
	 * @return
	 * @throws SQLException 
	 */

	public boolean insertComment(BookPOJO book, String author, int rate,String content) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(author);

		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO comment(idBook,author,rate,datetime,summary) VALUES(?,?,?,CURRENT_TIMESTAMP,?");
		ps.setInt(1, book.getId());
		ps.setString(2, author);
		ps.setInt(3, rate);
		ps.setString(4, content);
		return ps.executeUpdate() > 0;
	}

	public List<CommentImpl> getComments(BookPOJO pojo) throws SQLException {
		
		List<CommentImpl> content = new ArrayList<>();
		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM comment WHERE idBook=? order by datetime desc ");
		ps.setInt(1, pojo.getId());
		ResultSet rs = ps.executeQuery();
		rs.beforeFirst();
		
		while(rs.next()){
			
			content.add(new CommentImpl(rs.getString("author"),rs.getString(("summary"))));
			
		}
		rs.close();
		return content;
	}

}
