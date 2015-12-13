package fr.upem.rmirest.bilmancamp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

import com.beust.jcommander.JCommander;

import fr.upem.rmirest.bilmancamp.helpers.DBHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.models.LibraryImpl;
import utils.Constants;

public class ServerApplication {

	private static final String LIBRARY_CONFIG_FILE_PATH = "data/confFiles/dataset.json";
	private static final String USER_SET_FILE_PATH = "data/confFiles/userset.json";
	private static final String BOOK_SET_FILE_PATH = "data/confFiles/bookset.json";
	private static final String RATE_SET_FILE_PATH = "data/confFiles/rateset.json";

	private final CommandLineParser env;

	/**
	 * Represent a RMI server using given {@link CommandLineParser} to configure
	 * itself
	 * 
	 * @param parameters
	 *            the command line parameters
	 */
	public ServerApplication(CommandLineParser env) {

		this.env = Objects.requireNonNull(env);
	}

	/**
	 * Starts the server
	 * 
	 * @throws RemoteException
	 */
	private void start() throws RemoteException {

		Library lib = sharedObject();
		Registry registry;

		/* Create or use an existing registry */
		if (env.getHost().toLowerCase().equals(Constants.LocalHost)) {
			registry = LocateRegistry.createRegistry(env.getPort());
		} else {
			registry = LocateRegistry.getRegistry(env.getPort());
		}

		/* Reset the database */
		if (env.isReset()) {
			boolean success = DBHelper.embeddedDB().clear();
			System.out.println("Clear the database " + success);
		}

		/* Initialize the database */
		if (env.isInit()) {
			DBHelper.embeddedDB().clear();
			System.out.println("Clear the database.");
			LibraryImpl libImpl = (LibraryImpl) lib;
			
						
			try {
				libImpl.populateDatabase(USER_SET_FILE_PATH, BOOK_SET_FILE_PATH, RATE_SET_FILE_PATH);
			} catch (IOException e) {
				System.err.println("Unable to load the database initializers.");
				e.printStackTrace();
			}
		}

		registry.rebind("libraryService", lib);
	}

	/**
	 * Returned the current shared Object
	 * 
	 * @return {@link Library}
	 * @throws RemoteException
	 */
	private Library sharedObject() throws RemoteException {
		try {
			return LibraryImpl.createLibraryImpl(DBHelper.embeddedDB(), LIBRARY_CONFIG_FILE_PATH);
		} catch (IOException e) {
			throw new UncheckedIOException("The library config file could not be parsed : " + LIBRARY_CONFIG_FILE_PATH,
					e);
		}
	}

	public static void main(String[] args) throws RemoteException {

		// Available Options handler
		CommandLineParser parser = new CommandLineParser();

		// CommandParser
		new JCommander(parser, args);

		// start server
		new ServerApplication(parser).start();

		System.out.println(String.format(" Server started at %s on port %s  ...", parser.getHost(), parser.getPort()));
	}

}
