package application.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.User;

/**
 * PROXY to the server model. Using RMI API : Remote objects like Library, User
 * & Book.
 * 
 * @author Jefferson
 *
 */
public class ProxyModel {

	/* The remote library connected */
	private LibraryAsynchrone library;
	/* The current remote user connected */
	private UserAsynchrone userConnected;

	public void connectServer() throws MalformedURLException, RemoteException, NotBoundException {
		// Time out period to allow the GUI to cache more. (Animation caches
		// hints & stuffs of my module) You can put it to 0, it will only affect
		// the very firsts animations' speed.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		// TODO put it in a configuration file.
		Library library = (Library) Naming.lookup("rmi://localhost:8888/libraryService");
		this.library = LibraryAsynchrone.createLibraryAsynchrone(library);
	}

	public List<String> getLibraryStatus() {
		// TODO I NEED A LIBRARY METHOD FOR THIS !!
		ArrayList<String> list = new ArrayList<>();
		list.add("Teacher");
		list.add("Student");
		return list;
	}

	public User connectUser(String login, String password) throws IllegalArgumentException, RemoteException {
		Library library = this.library.getLibrary();
		User remoteUser = library.connect(login, password);
		userConnected = UserAsynchrone.createUserAsynchrone(remoteUser, library.getBookHistory(remoteUser));
		return userConnected.getUser();
	}

	public boolean addUser(String firstName, String lastName, int cardID, String password, String status)
			throws IllegalArgumentException, RemoteException {
		return library.getLibrary().addUser(status, firstName, lastName, cardID, password);
	}

	public UserAsynchrone getConnectedUser() {
		return userConnected;
	}
}