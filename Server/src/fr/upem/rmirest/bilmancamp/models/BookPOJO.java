package fr.upem.rmirest.bilmancamp.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import fr.upem.rmirest.bilmancamp.interfaces.BookComment;
import fr.upem.rmirest.bilmancamp.interfaces.Image;

public class BookPOJO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public BookPOJO(int id, String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage, List<Image> secondaryImages) {

		// Database fields
		this.id = id;
		date = LocalDate.now();
		// Model fields initialization
		this.title = Objects.requireNonNull(title);
		this.authors = Objects.requireNonNull(authors);
		this.summary = summary;
		this.categories = Objects.requireNonNull(categories);
		this.tags = Objects.requireNonNull(tags);
		this.mainImage = Objects.requireNonNull(mainImage);
		this.secondaryImages = secondaryImages;
		comments = new ArrayList<>();
		// TODO I guess constructors should not throw exceptions. Switch of a
		// factory method latter ?
		if (price < 0) {
			throw new IllegalArgumentException("The price of a book cannot be " + price);
		}
		this.price = price;
		this.totalRate = 0;
	}

	public BookPOJO(int id, String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, Image mainImage) {
		this(id, title, authors, summary, categories, price, tags, mainImage, new ArrayList<>());
	}

	public int getConsultationNumber() {
		return consultationNumber;
	}

	public void setConsultationNumber(int consultationNumber) {
		this.consultationNumber = consultationNumber;
	}

	public float getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(float totalRate) {
		this.totalRate = totalRate;
	}

	public int getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(int rateNumber) {
		this.rateNumber = rateNumber;
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public String getSummary() {
		return summary;
	}

	public List<String> getCategories() {
		return categories;
	}

	public List<String> getTags() {
		return tags;
	}

	public Image getMainImage() {
		return mainImage;
	}

	public List<Image> getSecondaryImages() {
		return secondaryImages;
	}

	public double getPrice() {
		return price;
	}

	public List<BookComment> getComments() {
		return Collections.unmodifiableList(comments);
	}

	public void addComment(BookComment comment) {
		comments.add(comment);
	}

	public void addSecondaryImages(List<Image> images) {
		secondaryImages.addAll(Objects.requireNonNull(images));
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
		if (!(obj instanceof BookPOJO)) {
			return false;
		}

		BookPOJO other = (BookPOJO) obj;

		return id == other.id;
	}

}
