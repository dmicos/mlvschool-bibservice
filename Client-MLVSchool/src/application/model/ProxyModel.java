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

	private Library library;

	public void connectServer() throws MalformedURLException, RemoteException, NotBoundException {
		// Time out period to allow the GUI to cache more. (Animation caches
		// hints stuffs) You can put it to 0, it will only affect the very
		// firsts animations' quality.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		library = (Library) Naming.lookup("rmi://localhost:8888/libraryService");
	}

	public List<String> getLibraryStatus() {
		// TODO I NEED A LIBRARY METHOD FOR THIS !!
		ArrayList<String> list = new ArrayList<>();
		list.add("Teacher");
		list.add("Student");
		return list;
	}

	public User connectUser(String login, String password) throws IllegalArgumentException, RemoteException {
		return library.connect(login, password);
	}

	public boolean addUser(String firstName, String lastName, int cardID, String password, String status)
			throws IllegalArgumentException, RemoteException {
		return library.addUser(status, firstName, lastName, cardID, password);
	}
}
