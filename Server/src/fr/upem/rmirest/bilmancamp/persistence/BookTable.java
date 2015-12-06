package fr.upem.rmirest.bilmancamp.persistence;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.rmirest.bilmancamp.helpers.ImageHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.models.BookImpl;

public class BookTable extends AbstractTableModel<Book> {

	public BookTable(Connection connection) {
		super(connection);
	}

	@Override
	public boolean insert(Book book) throws SQLException {

		Objects.requireNonNull(book);
		
		PreparedStatement ps = getConnection().prepareStatement(
				"INSERT INTO book(title,description,price,tags,viewCounter,authors,datetime,catName,image,ptags,stock) VALUES(?,?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		// fill the query blank
		ps.setString(1, book.getTitle());
		ps.setString(2, book.getSummary());
		ps.setDouble(3, book.getPrice());
		ps.setString(4, String.join(",", book.getTags()));
		ps.setDouble(5, book.getConsultationNumber());
		ps.setString(6, String.join(",", book.getAuthors()));
		ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
		ps.setString(8, String.join(",", book.getCategories()));
		ps.setString(9,
				String.join(",",
						Stream.concat(Stream.of(book.getMainImage().getPath()),
								book.getSecondaryImages().stream().map(img -> img.getPath()))
						.collect(Collectors.toList())));
		ps.setString(10, String.join(",", createKeyWords(book)));
		ps.setInt(11, 1);

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
	public List<Book> select() throws SQLException {

		List<Book> content = new ArrayList<>();
		Statement statement = getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM book ORDER BY title asc");
		extractFromResultSet(content, rs);
		return content;
	}

	@Override
	public List<Book> select(int offset, int limit) throws SQLException {

		List<Book> content = new ArrayList<>();
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM book  LIMIT ? OFFSET ? ORDER BY title asc");
		ps.setInt(1, limit);
		ps.setInt(1, offset);
		extractFromResultSet(content, ps.executeQuery());
		return content;
	}

	@Override
	public Optional<Book> find(Object... pk) throws SQLException {

		if (pk.length != 1)
			throw new IllegalArgumentException("Only one key is expected");

		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM book WHERE id=?");
		ps.setObject(1, pk[0]);
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			return Optional.of(extractRow(rs));
		}
		return Optional.empty();
	}

	@Override
	public List<Book> search(String... tags) throws SQLException {

		if (tags.length < 1)
			throw new IllegalArgumentException("You must give at least one keyword");

		List<String> lowTags = Arrays.asList(tags);
		lowTags.replaceAll(item -> "'%".concat(item.toLowerCase()).concat("%'"));

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM book WHERE LCASE(ptags) LIKE ")
				.append(String.join(" OR LCASE(ptags) LIKE ", lowTags)).append(" ORDER BY title");

		List<Book> content = new ArrayList<>();
		extractFromResultSet(content, getConnection().createStatement().executeQuery(builder.toString()));
		return content;
	}

	@Override
	public boolean update(Book oldVal, Book newVal) throws SQLException {

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
				String.join(",",
						Stream.concat(Stream.of(newVal.getMainImage().getPath()),
								newVal.getSecondaryImages().stream().map(img -> img.getPath()))
						.collect(Collectors.toList())));
		ps.setString(9, String.join(",", createKeyWords(newVal)));
		ps.setInt(10, newVal.getId());

		return ps.executeUpdate() > 0;
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
	 */
	public boolean borrow(Book book, User user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		PreparedStatement ps1 = getConnection().prepareStatement("UPDATE book SET stock = stock - 1");
		PreparedStatement ps2 = getConnection()
				.prepareStatement("INSERT INTO borrow(idUser,idBook,datetime) VALUES(?,?,CURRENT_TIMESTAMP)");
		ps2.setInt(1, user.getId());
		ps2.setInt(2, book.getId());

		return ps1.executeUpdate() > 0 && ps2.executeUpdate() > 0;
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
	public List<User> getQueue(Book book, int limit) throws SQLException {

		Objects.requireNonNull(book);

		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM queue q INNER JOIN user u ON q.idUser = user.id ORDER BY position desc LIMIT ?");
		ps.setInt(1, limit);

		return UserTable.extractUserFromResultSet(ps.executeQuery());
	}

	/**
	 * Add {@Link User} to the queue
	 * 
	 * @param book
	 *            the {@link Book} to borrow
	 * @param user
	 *            the {@link User} who wants to borrow
	 */
	public boolean addToQueue(Book book, User user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO queue(idUser,idBook) Values(?,?)");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());
		return ps.executeUpdate() > 0;

	}

	/**
	 * Remove {@Link User} from the queue
	 * 
	 * @param book
	 *            the {@link Book} borrowed
	 * @param user
	 *            the {@link User} to remove
	 */
	public void removeFromQueue(Book book, User user) throws SQLException {

		Objects.requireNonNull(book);
		Objects.requireNonNull(user);

		PreparedStatement ps = getConnection().prepareStatement("DELETE FROM queue WHERE idUser=?,idBook=?");
		ps.setInt(1, user.getId());
		ps.setInt(2, book.getId());
		ps.executeUpdate();

	}

	/**
	 * Check if given book is available
	 * 
	 * @param book
	 * @return
	 * @throws SQLException
	 */
	public boolean canBorrow(Book book) throws SQLException {

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
	 */
	public List<Book> selectMostConsulted(int number) throws SQLException {

		List<Book> content = new ArrayList<>();
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM book ORDER BY viewCounter desc  LIMIT ? ");
		ps.setInt(1, number);
		extractFromResultSet(content, ps.executeQuery());
		return content;
	}

	/***
	 * Create a list of key words from given {@link Book}
	 * 
	 * @param book
	 *            The {@link}
	 * @return a list of key words
	 */
	private List<String> createKeyWords(Book book) {

		List<String> kw = new ArrayList<>();
		kw.addAll(Arrays.asList(book.getTitle().split(" ")));
		kw.addAll(book.getCategories());
		kw.addAll(book.getTags());
		return kw;
	}

	/**
	 * Extract raw data from given {@link ResultSet}
	 * 
	 * @param dest
	 * @param rs
	 * @throws SQLException
	 */
	private void extractFromResultSet(List<Book> dest, ResultSet rs) throws SQLException {
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
	private Book extractRow(ResultSet rs) throws SQLException {

		// Load all stored images
		List<Image> images = ImageHelper.loadImage(Arrays.asList(rs.getString("image").split(",")).stream()
				.map(i -> Paths.get(i)).collect(Collectors.toList()));

		// Check and get secondaries if stored
		List<Image> secondaries = images.size() > 1 ? images.subList(1, images.size()) : Collections.emptyList();

		// Create the book
		return new BookImpl(rs.getInt("id"), rs.getString("title"), Arrays.asList(rs.getString("authors").split(",")),
				rs.getString("description"), Arrays.asList(rs.getString("catName").split(",")), rs.getDouble("price"),
				Arrays.asList(rs.getString("tags").split(",")), images.get(0), secondaries);

	}

}
