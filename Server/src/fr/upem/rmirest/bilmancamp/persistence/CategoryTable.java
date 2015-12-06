
package fr.upem.rmirest.bilmancamp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryTable extends AbstractTableModel<String> {

	public CategoryTable(Connection connection) {
		super(connection);
	}

	@Override
	public boolean insert(String obj) throws SQLException {
		String query = "INSERT INTO category VALUES(?)";
		PreparedStatement ps = getConnection().prepareStatement(query);
		ps.setString(1, obj);
		return ps.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Object... pk) throws SQLException {
		if (pk.length != 1)
			throw new IllegalArgumentException("Only one primary key is expected");

		String query = "DELETE FROM category WHERE catName=?";
		PreparedStatement ps = getConnection().prepareStatement(query);
		ps.setObject(1, pk[0]);
		return ps.executeUpdate() > 0;
	}

	@Override
	public List<String> select() throws SQLException {
		Statement st = getConnection().createStatement();
		return extractUserFromResultSet(st.executeQuery("SELECT * FROM category ORDER BY catName asc"));
	}

	@Override
	public List<String> select(int index, int limit) throws SQLException {
		PreparedStatement ps = getConnection()
				.prepareStatement("SELECT * FROM category ORDER BY catName asc LIMIT ? OFFSET ?");
		ps.setInt(1, limit);
		ps.setInt(2, index);
		return extractUserFromResultSet(ps.executeQuery());
	}

	@Override
	public Optional<String> find(Object... pk) throws SQLException {

		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM category Where catName=?");
		ps.setObject(1, pk[0]);
		ResultSet rs = ps.executeQuery();

		if (rs.first())
			return Optional.of(rs.getString("catName"));
		return Optional.empty();
	}

	@Override
	public List<String> search(String... tags) throws SQLException {
		List<String> cats = select();
		List<String> match = new ArrayList<>();

		for (String tag : tags) {
			match.addAll(cats.stream().filter(c -> c.toLowerCase().contains(tag)).collect(Collectors.toList()));
		}

		return match;
	}

	@Override
	public boolean update(String oldVal, String newVal) {
		throw new IllegalAccessError("Cannot update a primary key");
	}

	/**
	 * Retrieves data from given {@link ResultSet}
	 * 
	 * @param rs
	 *            the resulset
	 * @return a list of category name
	 * @throws SQLException
	 */
	private static List<String> extractUserFromResultSet(ResultSet rs) throws SQLException {

		List<String> dest = new ArrayList<>();
		rs.beforeFirst();
		while (rs.next()) {
			dest.add(rs.getString("catName"));
		}

		return dest;
	}
}
