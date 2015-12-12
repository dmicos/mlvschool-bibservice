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
public class LibraryAsynchrone {

	private final Library library;
	private final Map<String, String> descriptions;
	private final List<BookAsynchrone> bestBooks;
	private final List<BookAsynchrone> mostConsultedBooks;
	private final List<BookAsynchrone> mostRecentBooks;
	private final List<String> categories;

	private LibraryAsynchrone(Library library, List<BookAsynchrone> bestBooks, List<BookAsynchrone> mostConsultedBooks,
			List<BookAsynchrone> mostRecentBooks, List<String> categories, Map<String, String> descriptions) {
		this.bestBooks = bestBooks;
		this.mostConsultedBooks = mostConsultedBooks;
		this.mostRecentBooks = mostRecentBooks;
		this.library = library;
		this.categories = categories;
		this.descriptions = descriptions;
	}

	/**
	 * Has to be called in the thread reserved for RMI transfers.
	 * 
	 * @return an instance of {@link LibraryAsynchrone}.
	 */
	static LibraryAsynchrone createLibraryAsynchrone(Library library) throws RemoteException {
		// TODO implement every dynamic methods for
		// Categories/best/recent/consulted.
		Objects.requireNonNull(library);
		Map<String, String> descriptions = new HashMap<>();
		List<String> categories = library.getCategories();

		for (String category : categories) {
			descriptions.put(category, library.getCategoryDescription(category));
		}

		// Loading best books categories.
		List<BookAsynchrone> bestBooks = convertToBooksAsynchrone(library.getBestRatedBooks(5));
		List<BookAsynchrone> mostRecentBooks = convertToBooksAsynchrone(library.getMoreRecentBooks(5));
		List<BookAsynchrone> mostConsultedBooks = convertToBooksAsynchrone(library.getMostConsultedBooks(5));

		return new LibraryAsynchrone(library, bestBooks, mostConsultedBooks, mostRecentBooks, categories, descriptions);
	}

	public Library getLibrary() {
		return library;
	}

	public List<String> getCategories() {
		return Collections.unmodifiableList(categories);
	}

	public Map<String, String> getDescriptions() {
		return Collections.unmodifiableMap(descriptions);
	}

	public List<BookAsynchrone> getBestBooks() {
		return Collections.unmodifiableList(bestBooks);
	}

	public List<BookAsynchrone> getMostConsultedBooks() {
		return Collections.unmodifiableList(mostConsultedBooks);
	}

	public List<BookAsynchrone> getMostRecentBooks() {
		return Collections.unmodifiableList(mostRecentBooks);
	}

	private static List<BookAsynchrone> convertToBooksAsynchrone(List<Book> books) throws RemoteException {
		try {
			return books.stream().map(b -> {
				try {
					return BookAsynchrone.createBookAsynchrone(b);
				} catch (RemoteException e) {
					throw new UncheckedRemoteException(e);
				}
			}).collect(Collectors.toList());
		} catch (UncheckedRemoteException e) {
			throw e.getCause();
		}
	}
}
