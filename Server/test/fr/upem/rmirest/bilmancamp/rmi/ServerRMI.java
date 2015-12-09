package fr.upem.rmirest.bilmancamp.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import fr.upem.rmirest.bilmancamp.database.Database;
import fr.upem.rmirest.bilmancamp.database.EmbeddedDB;
import fr.upem.rmirest.bilmancamp.database.JavaDatabase;
import fr.upem.rmirest.bilmancamp.helpers.DBHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.models.LibraryImpl;
import utils.Constants;

public class ServerRMI {

	@Test
	public void serverRMITest() throws RemoteException, NotBoundException {

		// policy
		System.setProperty("java.security.policy", Constants.SECURITY_POLICY_PATH);

		// Secu Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// setup object
		Library lib = new LibraryImpl(implementation());
		Registry reg = LocateRegistry.createRegistry(8888); // Server Embedded
															// registry for
															// quick Test

		reg.rebind("lib", lib);

		// Tested in other file
		Library clientLib = (Library) reg.lookup("lib");

		// Check server stored data and client response data
		Assert.assertEquals(lib.getMoreRecentBooks(10), clientLib.getMoreRecentBooks(10));
		Assert.assertEquals(lib.getCategories(), clientLib.getCategories());
	}

	private static Database implementation() {

		try {
			return new EmbeddedDB(DBHelper.connect("jdbc:h2:" + Constants.DATABASE_PATH, "pony", "merens*30"));
		} catch (SQLException | ClassNotFoundException ex) {
			return new JavaDatabase();
		}
	}
}
