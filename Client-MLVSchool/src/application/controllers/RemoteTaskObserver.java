package application.controllers;

import java.util.List;

import application.model.BookAsynchrone;
import application.model.ProxyModel;

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

	public ProxyModel getProxyModel();
}
