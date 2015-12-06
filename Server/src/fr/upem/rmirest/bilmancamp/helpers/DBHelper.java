package fr.upem.rmirest.bilmancamp.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class DBHelper {

	private static final String DRIVER = "org.h2.Driver";
	
	/**
	 * 
	 * @param dbFilePath
	 *            the location of the database file
	 * @param username
	 *            the database user account
	 * @param password
	 *            the user's password
	 * @return
	 */
	public static Connection connect(String dbFilePath, String username, String password)
			throws ClassNotFoundException, SQLException {

		// Load Driver class
		Class.forName(DRIVER);

		// Try to connect to embedded db. If the db file is not found, it will
		// be generated but empty. We need to
		// re-create db script.
		return DriverManager.getConnection(Objects.requireNonNull(dbFilePath),
				Objects.requireNonNull(username), Objects.requireNonNull(password));
	}

}
