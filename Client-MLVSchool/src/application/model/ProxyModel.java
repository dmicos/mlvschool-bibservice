package application.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

	private enum State {
		OFFLINE, CONNECTED;
	}

	private State state;

	/* The remote library connected */
	private LibraryAsynchrone library;

	/* The current remote user connected */
	private UserAsynchrone userConnected;

	public ProxyModel() {
		state = State.OFFLINE;
	}

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

	public LibraryAsynchrone getLibrary() {
		return library;
	}

	public User connectUser(String login, String password) throws IllegalArgumentException, RemoteException {
		Library library = this.library.getLibrary();
		User remoteUser = library.connect(login, password);
		userConnected = UserAsynchrone.createUserAsynchrone(library, remoteUser, library.getBookHistory(remoteUser));
		state = State.CONNECTED;
		return userConnected.getRemoteUser();
	}

	public boolean addUser(String firstName, String lastName, int cardID, String password, String status)
			throws IllegalArgumentException, RemoteException {
		return library.getLibrary().addUser(status, firstName, lastName, cardID, password);
	}

	public UserAsynchrone getConnectedUser() {
		return userConnected;
	}

	/**
	 * Disconnects the current user account, if connected.
	 * 
	 * @throws RemoteException
	 */
	public void disconnectUser() throws RemoteException {
		if (state == State.CONNECTED) {
			userConnected.getRemoteUser().disconnect();
		}
	}

	public boolean addBook(String title, List<String> authors, String summary, String mainImage,
			List<String> secondaryImages, List<String> categories, double price, List<String> tags)
					throws IllegalArgumentException, RemoteException {
		library.getLibrary().addBook(title, authors, summary, mainImage, secondaryImages, categories, price, tags);
		library.reloadBooks();
		return true;
	}

	public List<BookAsynchrone> searchByCategory(String category) throws RemoteException {
		return BookAsynchrone.convertToBooksAsynchrone(library.getLibrary().getCategoryBooks(category));
	}

	public void reloadLibrary() throws RemoteException {
		library.reload();
	}

	public List<BookAsynchrone> search(String[] keywords) throws RemoteException {
		return BookAsynchrone.convertToBooksAsynchrone(library.getLibrary().searchBooks(keywords));
	}

	public BookAsynchrone updateBook(BookAsynchrone book) throws RemoteException {
		return book.update();
	}

	public boolean borrowBook(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().borrow(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update();
		return result;
	}

	public boolean giveBack(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().giveBack(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update();
		return result;
	}

	public boolean cancel(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().giveBack(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update();
		return result;
	}
}
