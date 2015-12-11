package fr.upem.rmirest.bilmancamp;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import utils.*;
import com.beust.jcommander.JCommander;

import fr.upem.rmirest.bilmancamp.helpers.DBHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.models.LibraryImpl;

public class ServerApplication {

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
			DBHelper.embeddedDB().clear();
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
		Library lib = new LibraryImpl(DBHelper.embeddedDB());
		
		return lib;
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
