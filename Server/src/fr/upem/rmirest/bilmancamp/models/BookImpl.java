package fr.upem.rmirest.bilmancamp.models;

import java.rmi.Remote;
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

public class BookImpl implements Book, Remote {

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
	private final Set<User> raters;

	public BookImpl(String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage, List<Image> secondaryImages) {
		super();
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

	// Getters

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public List<String> getAuthors() {
		return Collections.unmodifiableList(authors);
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public List<String> getCategories() {
		return Collections.unmodifiableList(categories);
	}

	@Override
	public int getConsultationNumber() {
		return consultationNumber;
	}

	@Override
	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}

	@Override
	public Image getMainImage() {
		return mainImage;
	}

	@Override
	public List<Image> getSecondaryImages() {
		return Collections.unmodifiableList(secondaryImages);
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public float getRate() {
		if (rateNumber == 0) {
			return 0;
		}
		return totalRate / rateNumber;
	}

	@Override
	public int getRateNumber() {
		return rateNumber;
	}

	@Override
	public boolean borrow(User user) {
		// If the book is available, let the user borrow it.
		if (borrower == null) {
			borrower = user;
			consultationNumber++;
			return true;
		}
		// Add in the queue only if the user is not the current borrower
		// and if it's not already into.
		if (!borrower.equals(user) && subscribers.contains(user)) {
			subscribers.add(user);
		}
		return false;
	}

	@Override
	public void rate(User user, int evaluation) throws IllegalArgumentException {
		if (evaluation < 0 || evaluation > 5) {
			throw new IllegalArgumentException("Evaluation of a book cannot be " + evaluation);
		}
		if (!raters.contains(user)) {
			raters.add(user);
			totalRate += evaluation;
			rateNumber += 1;
		}
	}

	@Override
	public void comment(BookComment bookComment) {
		comments.add(Objects.requireNonNull(bookComment));
	}

	@Override
	public void giveBack(User user) {
		if (borrower.equals(user)) {
			// If there is an user waiting for this book.
			if (!subscribers.isEmpty()) {
				User newBorrower = subscribers.pop();
				// TODO find a way to notify the Library that the book was
				// borrowed
				// by this user. (for borrowing history)
				// Borrowing history should not be stored into the User object
				// because of it's Database related nature.
				borrower = user;
			}
		}
	}

	@Override
	public void unregister(User user) {
		subscribers.remove(user);
	}

	@Override
	public int getRankInWaitingQueue(User user) {
		// TODO need some reallocations. Maybe change the ArrayDeque subscribers
		// to an ArrayList for improved complexity.
		return Arrays.asList(subscribers.toArray()).indexOf(user) + 1;
	}

	@Override
	public String toString() {
		return String.format("%s by %s", getTitle(), getAuthors());
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BookImpl)) {
			return false;
		}
		Book other = (Book) obj;

		if (!this.getTitle().equals(other.getTitle())) {
			return false;
		}
		if (!this.getAuthors().equals(other.getAuthors())) {
			return false;
		}

		if (!this.getSummary().equals(other.getSummary())) {
			return false;
		}
		if (!this.getCategories().equals(other.getCategories())) {
			return false;
		}
		if (!this.getTags().equals(other.getTags())) {
			return false;
		}
		if (this.getPrice() != other.getPrice()) {
			return false;
		}

		return true;
	}
	

}
