package fr.upem.rmirest.bilmancamp.persistence;

import java.sql.Connection;
import java.util.Objects;

public abstract class AbstractTableModel<T> implements TableModel<T> {

	private final Connection connection;

	/**
	 * 
	 * @param connection
	 *            the database connection
	 */
	public AbstractTableModel(Connection connection) {
		this.connection = Objects.requireNonNull(connection);
	}

	/**
	 * Get the current connection
	 * 
	 * @return {@link Connection}
	 */
	public Connection getConnection() {
		return connection;
	}

}
