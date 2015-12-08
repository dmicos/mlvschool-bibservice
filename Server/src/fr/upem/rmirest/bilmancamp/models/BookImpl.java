package fr.upem.rmirest.bilmancamp.models;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.BookComment;
import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.interfaces.User;

public class BookImpl implements Book {

	// Id counter
	private static int idCount = 1;

	// Database fields
	private final int id;
	private final LocalDate date;
	// Model fields
	private final String title;
	private final List<String> authors;
	private final String summary;
	private final List<String> categories;
	private int consultationNumber = 0;
	private final List<String> tags;
	private final Image mainImage;
	private final List<Image> secondaryImages;
	private final double price;
	private float totalRate;
	private int rateNumber;
	private final List<BookComment> comments;

	// Object fields
	private User borrower = null;
	private final ArrayDeque<User> subscribers;
	// TODO : Comment gérer les notations de livre ?
	private final Set<User> raters;

	public BookImpl(int id, String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage, List<Image> secondaryImages) {

		// Database fields
		this.id = id;
		idCount++;
		date = LocalDate.now();
		// Model fields initialization
		this.title = Objects.requireNonNull(title);
		this.authors = Objects.requireNonNull(authors);
		this.summary = summary;
		this.categories = Objects.requireNonNull(categories);
		this.tags = Objects.requireNonNull(tags);
		this.mainImage = Objects.requireNonNull(mainImage);
		this.secondaryImages = Objects.requireNonNull(secondaryImages);
		comments = new ArrayList<>();
		// TODO I guess constructors should not throw exceptions. Switch of a
		// factory method latter ?
		if (price < 0) {
			throw new IllegalArgumentException("The price of a book cannot be " + price);
		}
		this.price = price;
		this.totalRate = 0;
		this.rateNumber = 0;
		// Data fields initialization
		subscribers = new ArrayDeque<>();
		raters = new HashSet<>();
	}

	public BookImpl(String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage, List<Image> secondaryImages) {
		this(idCount, title, authors, summary, categories, price, tags, mainImage, Collections.emptyList());
	}

	public BookImpl(int id, String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage) {
		this(id, title, authors, summary, categories, price, tags, mainImage, Collections.emptyList());

	}

	// Getters

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public LocalDate getDate() throws RemoteException {
		return date.plusDays(0);
	}

	@Override
	public String getTitle() throws RemoteException {
		return title;
	}

	@Override
	public List<String> getAuthors() throws RemoteException {
		return Collections.unmodifiableList(authors);
	}

	@Override
	public String getSummary() throws RemoteException {
		return summary;
	}

	@Override
	public List<String> getCategories() throws RemoteException {
		return Collections.unmodifiableList(categories);
	}

	@Override
	public int getConsultationNumber() throws RemoteException {
		return consultationNumber;
	}

	@Override
	public List<String> getTags() throws RemoteException {
		return Collections.unmodifiableList(tags);
	}

	@Override
	public Image getMainImage() throws RemoteException {
		return mainImage;
	}

	@Override
	public List<Image> getSecondaryImages() throws RemoteException {
		return Collections.unmodifiableList(secondaryImages);
	}

	@Override
	public double getPrice() throws RemoteException {
		return price;
	}

	@Override
	public float getRate() throws RemoteException {
		if (rateNumber == 0) {
			return 0;
		}
		return totalRate / rateNumber;
	}

	@Override
	public int getRateNumber() throws RemoteException {
		return rateNumber;
	}

	@Override
	public List<BookComment> getComments() throws RemoteException {
		return Collections.unmodifiableList(comments);
	}

	@Override
	public boolean isAvailable() throws RemoteException {
		return borrower == null;
	}

	@Override
	public void comment(BookComment bookComment) throws RemoteException {
		comments.add(Objects.requireNonNull(bookComment));
	}

	@Override
	public void unregister(User user) throws RemoteException {
		subscribers.remove(user);
	}

	@Override
	public int getRankInWaitingQueue(User user) throws RemoteException {
		// TODO need some reallocations. Maybe change the ArrayDeque subscribers
		// to an ArrayList for improved complexity.
		return Arrays.asList(subscribers.toArray()).indexOf(user) + 1;
	}

	@Override
	public String toString() {
		return String.format("%s by %s", title, authors);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BookImpl)) {
			return false;
		}

		BookImpl other = (BookImpl) obj;

		return id == other.id;
	}

}
