package fr.upem.rmirest.bilmancamp.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.database.EmbeddedDB;
import fr.upem.rmirest.bilmancamp.database.JavaDatabase;
import utils.Constants;

public final class DBHelper {

	private static final String DRIVER = "org.h2.Driver";
	private static Database embeddedDB;

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

		Class.forName(DRIVER);

		return DriverManager.getConnection(Objects.requireNonNull(dbFilePath), Objects.requireNonNull(username),
				Objects.requireNonNull(password));
	}

	/**
	 * Return a {@link Database} of the tested implementation. Allows this junit
	 * test to be easily changed for every implementation.
	 * 
	 * @return a Database object of the tested class.
	 */
	public static Database embeddedDB() {

		if (embeddedDB != null)
			return embeddedDB;

		try {
			embeddedDB = new EmbeddedDB(DBHelper.connect("jdbc:h2:" + Constants.DATABASE_PATH, "pony", "merens*30"));
			return embeddedDB;
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			return new JavaDatabase();
		}
	}

}
