package application.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import application.ClientMLVSchool;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
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
		Library library = (Library) Naming
				.lookup("rmi://" + ClientMLVSchool.getINSTANCE().getRMIAddress() + "libraryService");
		this.library = LibraryAsynchrone.createLibraryAsynchrone(library);
	}

	public LibraryAsynchrone getLibrary() {
		return library;
	}

	public User connectUser(String login, String password, MailBox<Book> mailBox)
			throws IllegalArgumentException, RemoteException {
		Library library = this.library.getLibrary();
		User remoteUser = library.connect(login, password, mailBox);
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
		return BookAsynchrone.convertToBooksAsynchrone(library.getLibrary(),
				library.getLibrary().getCategoryBooks(category));
	}

	public void reloadLibrary() throws RemoteException {
		library.reload();
	}

	public List<BookAsynchrone> search(String[] keywords) throws RemoteException {
		return BookAsynchrone.convertToBooksAsynchrone(library.getLibrary(),
				library.getLibrary().searchBooks(keywords));
	}

	public BookAsynchrone updateBook(BookAsynchrone book) throws RemoteException {
		return book.update(library.getLibrary());
	}

	public boolean borrowBook(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().borrow(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update(library.getLibrary());
		return result;
	}

	public boolean giveBack(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().giveBack(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update(library.getLibrary());
		return result;
	}

	public boolean cancel(UserAsynchrone user, BookAsynchrone book) throws RemoteException {
		boolean result = library.getLibrary().cancelRegistration(book.getRemoteBook(), user.getRemoteUser());
		user.update(library);
		book.update(library.getLibrary());
		return result;
	}

	public Boolean addComment(BookAsynchrone book, String content, int rate) throws RemoteException {
		Book remoteBook = book.getRemoteBook();
		User remoteUser = userConnected.getRemoteUser();
		String name = userConnected.getFirstName() + " " + userConnected.getLastName();
		// Workaround. Should have been only 1 call.
		boolean addComment = library.getLibrary().addComment(remoteBook, name, rate, content);
		boolean rateBook = library.getLibrary().rateBook(remoteBook, remoteUser, rate);
		userConnected.update(library);
		book.update(library.getLibrary());
		return addComment && rateBook;
	}
}
