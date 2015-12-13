package application.model;

import static application.model.BookAsynchrone.convertToBooksAsynchrone;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	private final List<String> status;

	private LibraryAsynchrone(Library library, List<BookAsynchrone> bestBooks, List<BookAsynchrone> mostConsultedBooks,
			List<BookAsynchrone> mostRecentBooks, List<String> categories, Map<String, String> descriptions,
			List<String> status) {
		this.bestBooks = bestBooks;
		this.mostConsultedBooks = mostConsultedBooks;
		this.mostRecentBooks = mostRecentBooks;
		this.library = library;
		this.categories = categories;
		this.descriptions = descriptions;
		this.status = status;
	}

	/**
	 * Has to be called in the thread reserved for RMI transfers.
	 * 
	 * @return an instance of {@link LibraryAsynchrone}.
	 */
	static LibraryAsynchrone createLibraryAsynchrone(Library library) throws RemoteException {
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

		// What about creating these status dynamically ?
		List<String> status = new ArrayList<>();
		status.add("Teacher");
		status.add("Student");
		return new LibraryAsynchrone(library, bestBooks, mostConsultedBooks, mostRecentBooks, categories, descriptions,
				status);
	}

	public void reloadBooks() throws RemoteException {
		bestBooks.clear();
		bestBooks.addAll(convertToBooksAsynchrone(library.getBestRatedBooks(5)));
		mostRecentBooks.clear();
		mostRecentBooks.addAll(convertToBooksAsynchrone(library.getMoreRecentBooks(5)));
		mostConsultedBooks.clear();
		mostConsultedBooks.addAll(convertToBooksAsynchrone(library.getMostConsultedBooks(5)));
	}

	/**
	 * Reloads all the data.
	 * @throws RemoteException 
	 */
	public void reload() throws RemoteException {
		reloadBooks();
	}

	Library getLibrary() {
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

	public List<String> getStatus() {
		return status;
	}
}
