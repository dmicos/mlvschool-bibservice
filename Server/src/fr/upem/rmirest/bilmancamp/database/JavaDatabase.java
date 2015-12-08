package fr.upem.rmirest.bilmancamp.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.upem.rmirest.bilmancamp.helpers.UserHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import fr.upem.rmirest.bilmancamp.interfaces.User;

/**
 * This implementation of {@link Database} uses Java objects to keep data
 * instead of a real database.
 */
public class JavaDatabase implements Database {

	/**
	 * Simulate a DB table for books.
	 */
	static class BookTable {

		private final List<BookEntry> books = new ArrayList<>();

		/**
		 * Inner class to represent a Book Entry which contains the Book and the
		 * timestamp for it's addition to the "database".
		 */
		class BookEntry {
			private final long timestamp;
			private final Book book;

			public BookEntry(Book book) {
				this.book = book;
				timestamp = System.nanoTime();
			}

			public long getTimestamp() {
				return timestamp;
			}

			public Book getBook() {
				return book;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (!(obj instanceof BookEntry)) {
					return false;
				}
				BookEntry other = (BookEntry) obj;
				return getBook().equals(other.getBook());
			}

		}

		public void add(Book book) {
			books.add(new BookEntry(Objects.requireNonNull(book)));
		}

		public List<BookEntry> getAll() {
			return Collections.unmodifiableList(books);
		}

		public List<Book> searchFromKeywords(String... keywords) {
			return books.stream().map(be -> be.getBook()).filter(book -> matchKeywords(book, keywords))
					.collect(Collectors.toList());
		}

		public List<Book> getMostSimilar(Book refBook, int number) {
			// Get all the book's keyword.
			List<String> bookKeywords = getBookKeywords(refBook);
			Set<String> initial = new HashSet<>();
			initial.addAll(bookKeywords);

			// Very big and non-efficient stream which keep only the "number"
			// most similar books.
			return getAll().stream().filter(aBook -> !aBook.getBook().equals(refBook)).sorted((b1, b2) -> {
				Set<String> kb1 = getBookKeywords(b1.getBook()).stream().map(s -> s.toLowerCase())
						.collect(Collectors.toSet());
				kb1.retainAll(initial);
				Set<String> kb2 = getBookKeywords(b2.getBook()).stream().map(s -> s.toLowerCase())
						.collect(Collectors.toSet());
				kb2.retainAll(initial);
				return kb2.size() - kb1.size();
			}).filter(book -> {
				Set<String> kWords = getBookKeywords(book.getBook()).stream().map(s -> s.toLowerCase())
						.collect(Collectors.toSet());
				kWords.retainAll(initial);
				return !kWords.isEmpty();
			}).limit(number).map(be -> be.getBook()).collect(Collectors.toList());

		}

		/**
		 * Indicates whenever the given book match of the the given keywords.
		 * 
		 * @param book
		 * @param keywords
		 * @return <code>true</code> if the given book match at least a keyword.
		 */
		private static boolean matchKeywords(Book book, String... keywords) {
			// Create as list of all Book related words.
			List<String> bw = getBookKeywords(book);

			// Put all the words in lower case.
			List<String> lowerKeywords = Arrays.asList(keywords).stream().map(s -> s.toLowerCase())
					.collect(Collectors.toList());

			// Check if one of those words is into the keywords.
			for (String keyword : lowerKeywords) {
				if (bw.contains(keyword)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Get all the keywords related to a book. From it's tags, categories,
		 * and title parts.
		 * 
		 * @param book
		 * @return a list of keywords.
		 */
		private static List<String> getBookKeywords(Book book) {
			List<String> bw = new ArrayList<>();
			// Get all the book's related words.
			bw.addAll(book.getCategories());
			bw.addAll(book.getTags());
			bw.addAll(Arrays.asList(book.getTitle().split(" ")));
			// Put them in lower case and return it.
			return bw.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
		}

	}

	static class UserTable {

		private final Map<String, User> users = new HashMap<>();

		public boolean add(User user) {
			String computedId = UserHelper.computeId(user);
			if (users.containsKey(computedId)) {
				return false;
			}
			users.put(computedId, user);
			return true;
		}

		public User connect(String id, String password) {
			if (!users.containsKey(id)) {
				return null;
			}
			User user = users.get(id);
			if (user.isLoginValid(id, password)) {
				return user;
			}
			return null;
		}

	}

	class CategoryTable {

		// TODO Categories loading from a file.
		private final List<String> categories = new ArrayList<>();

		public List<String> getAll() {
			return Collections.unmodifiableList(categories);
		}

	}

	private final BookTable bTable = new BookTable();
	private final UserTable uTable = new UserTable();
	private final CategoryTable cTable = new CategoryTable();

	@Override
	public boolean addBook(Book book) {
		bTable.add(book);
		return true;
	}

	@Override
	public boolean addUser(User user) {
		return uTable.add(user);
	}

	@Override
	public User connectUser(String id, String password) {
		return uTable.connect(id, password);
	}

	@Override
	public List<Book> searchBookFromKeywords(String... keywords) {
		return bTable.searchFromKeywords(keywords);
	}

	@Override
	public List<String> getCategories() {
		return Collections.unmodifiableList(cTable.getAll());
	}

	@Override
	public List<Book> getBookFromCategory(String category) {
		return bTable.getAll()
				.stream().map(be -> be.getBook()).filter(book -> book.getCategories().stream()
						.map(cat -> cat.toLowerCase()).collect(Collectors.toList()).contains(category))
				.collect(Collectors.toList());
	}

	@Override
	public List<Book> getBookRecents(int number) {
		return bTable.getAll().stream().sorted((b1, b2) -> (int) (b2.getTimestamp() - b1.getTimestamp())).limit(number)
				.map(be -> be.getBook()).collect(Collectors.toList());
	}

	@Override
	public List<Book> getBookBestRate(int number) {
		return bTable.getAll().stream().map(be -> be.getBook()).sorted((b1, b2) -> (int) (b2.getRate() - b1.getRate()))
				.limit(number).collect(Collectors.toList());
	}

	@Override
	public List<Book> getBookMostConsulted(int number) {
		return bTable.getAll().stream().map(be -> be.getBook())
				.sorted((b1, b2) -> b2.getConsultationNumber() - b1.getConsultationNumber()).limit(number)
				.collect(Collectors.toList());
	}

	@Override
	public List<Book> getBookMostSimilar(Book book, int number) {
		return bTable.getMostSimilar(book, number);
	}

	@Override
	public boolean borrow(Book book, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> getQueue(Book book, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addToQueue(User user, Book book) {
		return false;

	}

	@Override
	public boolean isAvailable(Book book) {
		return book.isAvailable();
	}

	@Override
	public boolean addUser(User user, String password) {
		return addUser(user);
	}

	@Override
	public boolean rateBook(Book book, User user, int value) {
		book.rate(user, value);
		return false;
	}

	@Override
	public User connectUser(String id, String password, MailBox<Book> callback) {
		return connectUser(id, password);
	}

	@Override
	public boolean giveBack(Book book, User user) {
		book.giveBack(user);
		return true;
	}

}
