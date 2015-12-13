package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import application.utils.UncheckedRemoteException;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import javafx.application.Platform;

public class MailBoxImpl extends UnicastRemoteObject implements MailBox<Book> {

	protected MailBoxImpl() throws RemoteException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void receive(Book value) throws RemoteException {
		try {
			Platform.runLater(() -> {
				try {
					NotificationsManager.notify("New book available !", value.getTitle(), NotificationType.INFO);
				} catch (RemoteException e) {
					throw new UncheckedRemoteException(e);
				}
			});
		} catch (UncheckedRemoteException e) {
			throw e.getCause();
		}
	}
}
