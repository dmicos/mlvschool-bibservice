package fr.upem.rmirest.bilmancamp.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.database.EmbeddedDB;
import utils.Constants;

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

		String sharedPath = System.getProperty(Constants.DB_HOME);

		if (sharedPath == null) {
			sharedPath = Constants.DATABASE_PATH;
			//saveHomeDir(sharedPath);
		}

		try {
			Database db =  new EmbeddedDB(connect("jdbc:h2:" + sharedPath, "pony", "merens*30"));
			return db;
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Add a shared property
	 * 
	 * @param path
	 */
	public static void saveHomeDir(String path) {

		Properties props = new Properties();
		props.put("DB_HOME", path);
		System.setProperties(props);
	}

}
