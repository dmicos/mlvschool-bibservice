package application.model;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import application.utils.UncheckedRemoteException;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Library;

/**
 * Wrapper of a remote Library (RMI). Allows the caching of static value like
 * getCategories and so one to prevent multiple requests & simplifying the GUI
 * part.
 * 
 * @author Jefferson
 *
 */
class LibraryAsynchrone {

	private final Library library;
	private final Map<String, String> categories;

	private LibraryAsynchrone(Library library, List<BookAsynchrone> books, List<BookAsynchrone> mostConsultedBooks,
			List<BookAsynchrone> mostRecentBooks, Map<String, String> categories) {
		this.library = Objects.requireNonNull(library);
		this.categories = Objects.requireNonNull(categories);
	}

	/**
	 * Has to be called in the thread reserved for RMI transfers.
	 * 
	 * @return an instance of {@link LibraryAsynchrone}.
	 */
	static LibraryAsynchrone createLibraryAsynchrone(Library library) throws RemoteException {
		Map<String, String> categories = new HashMap<>();
		for (String category : library.getCategories()) {
			// TODO implement Library::getDescription:String.
			// descriptions.add(library.getDescription(category));
			categories.put(category, "Every books, specially for the STAPS section..");
		}

		// Loading best books categories.
		List<BookAsynchrone> bestBooks = convertToBooksAsynchrone(library.getBestRatedBooks(5));
		List<BookAsynchrone> mostRecentBooks = convertToBooksAsynchrone(library.getMoreRecentBooks(5));
		List<BookAsynchrone> mostConsultedBooks = convertToBooksAsynchrone(library.getMostConsultedBooks(5));

		return new LibraryAsynchrone(library, bestBooks, mostConsultedBooks, mostRecentBooks, categories);
	}

	public Library getLibrary() {
		return library;
	}

	public Map<String, String> getCategories() {
		return Collections.unmodifiableMap(categories);
	}

	private static List<BookAsynchrone> convertToBooksAsynchrone(List<Book> books) throws RemoteException {
		try {
			return books.stream().map(b -> BookAsynchrone.createBookAsynchrone(b)).collect(Collectors.toList());
		} catch (UncheckedRemoteException e) {
			throw e.getCause();
		}
	}
}
