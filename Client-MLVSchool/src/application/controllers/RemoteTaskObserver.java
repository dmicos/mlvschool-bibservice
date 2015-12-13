package application.controllers;

import java.util.List;

import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;

public interface RemoteTaskObserver {

	/**
	 * Notifies the observer that the library cache has been refreshed.
	 */
	default public void onLibraryRefreshed() {
	}

	/**
	 * Notifies the observer with the list of books found, and the keywords of
	 * the search.
	 */
	default public void onBookFound(List<BookAsynchrone> books, String[] keywords) {
	}

	/**
	 * Notifies that this book has been update and is ready to be shown in
	 * detail.
	 */
	public default void onBookVisualized(BookAsynchrone book) {
	}

	/**
	 * Notifies that a commentary has been done on the given book.
	 */
	public default void onCommentaryOnBookPosted(BookAsynchrone book) {

	}

	public ProxyModel getProxyModel();

	/**
	 * Notifies the user has requested the borrow of a book. The user is
	 * updated, and value represents if the book has been borrowed.
	 */
	public default void onBookBorrowed(Boolean borrowed, BookAsynchrone book, UserAsynchrone user) {
		if (borrowed) {
			NotificationsManager.notify("Information", "Book borrowed : " + book.getTitle(), NotificationType.INFO);
			return;
		}
		NotificationsManager.notify("Information", "You are in a queue for the book : " + book.getTitle(),
				NotificationType.INFO);
	}

	/**
	 * Notifies the user has given back one of its books if giveBack is true,
	 * failed if false.
	 */
	public default void onBookGivenBack(Boolean giveBack, BookAsynchrone book, UserAsynchrone user) {
		if (giveBack) {
			NotificationsManager.notify("Information", "Book given back : " + book.getTitle(), NotificationType.INFO);
			return;
		}
		NotificationsManager.notify("Information", "The book has not been released : " + book.getTitle(),
				NotificationType.INFO);
	}

	/**
	 * Notifies the user has canceled one of its books if cancel is true,
	 * failed if false.
	 */
	public default void onBookCancel(Boolean cancel, BookAsynchrone book, UserAsynchrone user) {
		if (cancel) {
			NotificationsManager.notify("Information", "Your are not in the queue anymore : " + book.getTitle(), NotificationType.INFO);
			return;
		}
		NotificationsManager.notify("Information", "Your are still in the queue : " + book.getTitle(),
				NotificationType.INFO);
	}
}
