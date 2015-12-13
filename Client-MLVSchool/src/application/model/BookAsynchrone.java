package application.model;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import application.utils.UncheckedRemoteException;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.BookComment;
import fr.upem.rmirest.bilmancamp.interfaces.Library;

public class BookAsynchrone {

	private String title;
	private String image;
	private List<String> authors;
	private int rate;
	private String date;
	private Book book;
	private final int id;
	private final List<BookComment> comments;
	private final String summary;

	public BookAsynchrone(Book book, int id, String title, List<String> authors, String date, int rate,
			List<BookComment> comments, String summary, String image) {
		this.book = book;
		this.id = id;
		this.authors = authors;
		this.date = date;
		this.rate = rate;
		this.comments = comments;
		this.summary = summary;
		this.title = Objects.requireNonNull(title);
		this.image = Objects.requireNonNull(image);

		// Workaround for the moment.
	}

	/**
	 * Has to be called in the thread reserved for RMI transfers.
	 * 
	 * @throws RemoteException
	 */
	public static BookAsynchrone createBookAsynchrone(Library library, Book book) throws RemoteException {
		String title = book.getTitle();
		String mainImage = book.getMainImage();
		List<String> authors = book.getAuthors();
		String date = book.getDate().toString();
		int rate = (int) library.getRate(book);
		int id = book.getId();
		List<BookComment> comment = library.getComment(book);
		String summary = book.getSummary();
		return new BookAsynchrone(book, id, title, authors, date, rate, comment, summary, mainImage);
	}

	BookAsynchrone update(Library library) throws RemoteException {
		title = book.getTitle();
		image = book.getMainImage();
		authors = book.getAuthors();
		date = book.getDate().toString();
		rate = (int) library.getRate(book);
		comments.clear();
		comments.addAll(library.getComment(book));
		return this;
	}

	public List<BookComment> getComments() {
		return comments;
	}

	public String getSummary() {
		return summary;
	}

	public Book getRemoteBook() {
		return book;
	}

	public String getTitle() {
		return title;
	}

	public int getRate() {
		return rate;
	}

	public List<String> getAuthors() {
		return Collections.unmodifiableList(authors);
	}

	public String getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public static List<BookAsynchrone> convertToBooksAsynchrone(Library library, List<Book> books)
			throws RemoteException {
		try {
			return books.stream().map(b -> {
				try {
					return BookAsynchrone.createBookAsynchrone(library, b);
				} catch (RemoteException e) {
					throw new UncheckedRemoteException(e);
				}
			}).collect(Collectors.toList());
		} catch (UncheckedRemoteException e) {
			throw e.getCause();
		}
	}

	public static final String DEBUG = "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";
}
