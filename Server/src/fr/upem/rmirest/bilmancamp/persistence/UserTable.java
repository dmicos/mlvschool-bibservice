package fr.upem.rmirest.bilmancamp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import fr.upem.rmirest.bilmancamp.helpers.UserHelper;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.models.UserImpl;

public class UserTable extends AbstractTableModel<User> {

	public UserTable(Connection connection) {
		super(connection);
	}

	@Override
	public boolean insert(User obj) throws SQLException {
		return insert(obj, UserHelper.generatePassword());
	}

	public boolean insert(User user, String password) throws SQLException {

		Objects.requireNonNull(user);
		Objects.requireNonNull(password);

		if (!loginIsAvailable(UserHelper.computeId(user)))
			return false;

		return prepareUser(user, password).executeUpdate() > 0;
	}

	@Override
	public boolean delete(Object... pk) throws SQLException {

		if (pk.length != 1)
			throw new IllegalArgumentException("Only one primary key is expected");

		PreparedStatement ps = getConnection().prepareStatement("UPDATE user set loggable=0 WHERE id=?");
		ps.setObject(1, pk[0]);

		return ps.executeUpdate() > 0;
	}

	@Override
	public List<User> select() throws SQLException {

		Statement st = getConnection().createStatement();
		return extractUserFromResultSet(
				st.executeQuery("SELECT * FROM user ORDER BY name asc,fname asc WHERE loggable = 1"));
	}

	@Override
	public List<User> select(int index, int limit) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement(
				"SELECT * FROM user WHERE loggable = 1  ORDER BY name asc,fname asc LIMIT ? OFFSET ?");
		ps.setInt(1, limit);
		ps.setInt(2, index);
		return extractUserFromResultSet(ps.executeQuery());
	}

	@Override
	public Optional<User> find(Object... pk) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM user WHERE id=? AND loggable = 1");
		ps.setObject(1, pk[0]);
		ResultSet rs = ps.executeQuery();

		if (rs.first())
			return Optional.of(extractRow(rs));

		return Optional.empty();
	}

	@Override
	public List<User> search(String... tags) throws SQLException {

		if (tags.length < 1)
			throw new IllegalArgumentException("You must give at least one keyword");

		String col1 = "LCASE(name) LIKE ";
		String col2 = "LCASE(fname) LIKE ";

		List<String> lowTags = Arrays.asList(tags);
		lowTags.replaceAll(item -> "'%".concat(item.toLowerCase()).concat("%'"));

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM user WHERE ").append(col1)
				.append(String.join(" OR ? ".replace("?", col1), lowTags)).append(" OR ? ".replace("?", col2))
				.append(String.join(" OR ? ".replace("?", col2), lowTags)).append(" ORDER BY name asc,fname asc");

		return extractUserFromResultSet(getConnection().createStatement().executeQuery(builder.toString()));
	}

	@Override
	public boolean update(User oldVal, User newVal) throws SQLException {

		Objects.requireNonNull(oldVal);
		Objects.requireNonNull(newVal);

		if (oldVal.equals(newVal))
			return false;

		String query = "UPDATE user SET fname=?,name=?,status=?,cardNumber=?,login=? WHERE id=?";
		PreparedStatement ps = getConnection().prepareStatement(query);
		ps.setString(1, newVal.getFirstName());
		ps.setString(2, newVal.getLastName());
		ps.setString(3, newVal.getStatus());
		ps.setInt(4, newVal.getCardNumber());
		ps.setString(5, UserHelper.computeId(newVal));
		ps.setInt(1, oldVal.getId());
		return ps.executeUpdate() > 0;
	}

	/**
	 * Prepare SQL query
	 * 
	 * @param obj
	 *            The {@link User} to add
	 * @param password
	 *            The user's password
	 * @return A {@link PreparedStatement}
	 * @throws SQLException
	 */
	private PreparedStatement prepareUser(User obj, String password) throws SQLException {

		String query = "INSERT INTO user(fname,name,status,cardNumber,datetime,login,loggable,password) VALUES(?,?,?,?,CURRENT_TIMESTAMP,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(query);
		ps.setString(1, obj.getFirstName());
		ps.setString(2, obj.getLastName());
		ps.setString(3, obj.getStatus());
		ps.setInt(4, obj.getCardNumber());
		ps.setString(5, UserHelper.computeId(obj));
		ps.setBoolean(6, true);
		ps.setString(7, password);

		return ps;
	}

	/**
	 * Check if a user exists or not
	 * 
	 * @param login
	 *            The user login
	 * @param password
	 *            The user password
	 * @return The {@link User} if found otherwise an empty {@link Optional<
	 *         {@link User}}
	 * @throws SQLException
	 */
	public Optional<User> exist(String login, String password) throws SQLException {
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM user WHERE login=? AND password=? AND loggable = 1");
		ps.setString(1, login);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();

		if (rs.first())
			return Optional.of(extractRow(rs));

		return Optional.empty();
	}

	/**
	 * Check if a user exists or not
	 * 
	 * @param login
	 *            The user login
	 * 
	 * @return The {@link User} if found otherwise empty element
	 * @throws SQLException
	 */
	public boolean loginIsAvailable(String login) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM user WHERE login=? AND loggable = 1");
		ps.setString(1, login);
		return !ps.executeQuery().first();
	}

	public static List<User> extractUserFromResultSet(ResultSet rs) throws SQLException {

		List<User> dest = new ArrayList<>();
		rs.beforeFirst();
		while (rs.next()) {
			dest.add(extractRow(rs));
		}

		return dest;
	}

	private static User extractRow(ResultSet rs) throws SQLException {

		return new UserImpl(rs.getInt("id"), rs.getString("status"), rs.getString("fname"), rs.getString("name"),
				rs.getString("password"), rs.getInt("cardNumber"));
	}

}
