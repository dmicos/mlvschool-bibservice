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

import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

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
			private final BookPOJO book;

			public BookEntry(BookPOJO book) {
				this.book = book;
				timestamp = System.nanoTime();
			}

			public long getTimestamp() {
				return timestamp;
			}

			public BookPOJO getBook() {
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

		public void add(BookPOJO book) {
			books.add(new BookEntry(Objects.requireNonNull(book)));
		}

		public List<BookEntry> getAll() {
			return Collections.unmodifiableList(books);
		}

		public List<BookPOJO> searchFromKeywords(String... keywords) {
			return books.stream().map(be -> be.getBook()).filter(book -> matchKeywords(book, keywords))
					.collect(Collectors.toList());
		}

		public List<BookPOJO> getMostSimilar(BookPOJO refBook, int number) {
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
		private static boolean matchKeywords(BookPOJO book, String... keywords) {
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
		private static List<String> getBookKeywords(BookPOJO book) {
			/*
			 * List<String> bw = new ArrayList<>(); // Get all the book's
			 * related words. bw.addAll(book.getCategories());
			 * bw.addAll(book.getTags());
			 * bw.addAll(Arrays.asList(book.getTitle().split(" "))); // Put them
			 * in lower case and return it. return bw.stream().map(s ->
			 * s.toLowerCase()).collect(Collectors.toList());
			 */

			throw new UnsupportedOperationException();
		}

	}

	static class UserTable {

		private final Map<String, UserPOJO> users = new HashMap<>();

		public boolean add(UserPOJO user) {
			// String computedId = UserHelper.computeId(user);
			// if (users.containsKey(computedId)) {
			// return false;
			// }
			// users.put(computedId, user);
			// return true;

			throw new UnsupportedOperationException();

		}

		public UserPOJO connect(String id, String password) {

			// if (!users.containsKey(id)) {
			// return null;
			// }
			// User user = users.get(id);
			// if (user.isLoginValid(id, password)) {
			// return user;
			// }
			// return null;

			throw new UnsupportedOperationException();

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
	public boolean addBook(BookPOJO book) {
		bTable.add(book);
		return true;
	}

	@Override
	public boolean addUser(UserPOJO user) {
		return uTable.add(user);
	}

	@Override
	public UserPOJO connectUser(String id, String password) {
		return uTable.connect(id, password);
	}

	@Override
	public List<BookPOJO> searchBookFromKeywords(String... keywords) {
		return bTable.searchFromKeywords(keywords);
	}

	@Override
	public boolean addCategory(String category) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getCategories() {
		return Collections.unmodifiableList(cTable.getAll());
	}

	@Override
	public List<BookPOJO> getBookFromCategory(String category) {

		throw new UnsupportedOperationException();

		// return bTable.getAll()
		// .stream().map(be -> be.getBook()).filter(book ->
		// book.getCategories().stream()
		// .map(cat ->
		// cat.toLowerCase()).collect(Collectors.toList()).contains(category))
		// .collect(Collectors.toList());
	}

	@Override
	public List<BookPOJO> getBookRecents(int number) {
		return bTable.getAll().stream().sorted((b1, b2) -> (int) (b2.getTimestamp() - b1.getTimestamp())).limit(number)
				.map(be -> be.getBook()).collect(Collectors.toList());
	}

	@Override
	public List<BookPOJO> getBookBestRate(int number) {
		// return bTable.getAll().stream().map(be -> be.getBook()).sorted((b1,
		// b2) -> (int) (b2.getRate() - b1.getRate()))
		// .limit(number).collect(Collectors.toList());

		throw new UnsupportedOperationException();

	}

	@Override
	public List<BookPOJO> getBookMostConsulted(int number) {
		// return bTable.getAll().stream().map(be -> be.getBook())
		// .sorted((b1, b2) -> b2.getConsultationNumber() -
		// b1.getConsultationNumber()).limit(number)
		// .collect(Collectors.toList());
		//
		throw new UnsupportedOperationException();

	}

	@Override
	public List<BookPOJO> getBookMostSimilar(BookPOJO book, int number) {
		return bTable.getMostSimilar(book, number);
	}

	@Override
	public boolean addUser(UserPOJO user, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BookPOJO> getBorrowedBooks(UserPOJO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean borrow(BookPOJO book, UserPOJO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAvailable(BookPOJO book) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserPOJO> getQueue(BookPOJO book, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addToQueue(UserPOJO user, BookPOJO book) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rateBook(BookPOJO book, UserPOJO user, int value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean giveBack(BookPOJO book, UserPOJO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFromQueue(BookPOJO book, UserPOJO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BookPOJO> hasBookInWait(UserPOJO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean clear() {
		uTable.users.clear();
		return true;
	}

}
