
package fr.upem.rmirest.bilmancamp.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BookPOJO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090961306833130244L;
	// Database fields
	private final int id;
	// Model fields
	private final String title;
	private final List<String> authors;
	private final String summary;
	private final List<String> categories;
	private int consultationNumber = 0;
	private final List<String> tags;
	private final RealImage mainImage;
	private final List<RealImage> secondaryImages;
	private final double price;
	private float totalRate;
	private int rateNumber;
	private final List<CommentImpl> comments;

	// WS Required one
	public BookPOJO() {

		id = 0;
		title = null;
		summary = null;
		tags = Collections.emptyList();
		secondaryImages = Collections.emptyList();
		comments = Collections.emptyList();
		categories = Collections.emptyList();
		price = 0;
		mainImage = null;
		LocalDate.now();
		authors = Collections.emptyList();
	}

	public BookPOJO(int id, String title, List<String> authors, String summary, List<String> categories, double price,
			List<String> tags, RealImage mainImage, List<RealImage> secondaryImages) {

		// Database fields
		this.id = id;
		LocalDate.now();
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
			List<String> tags, RealImage mainImage) {
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

	public String getTitle() {
		return title;
	}

	public String[] getAuthors() {
		return authors.toArray(new String[0]);
	}

	public String getSummary() {
		return summary;
	}

	public String[] getCategories() {
		return categories.toArray(new String[0]);
	}

	public String[] getTags() {
		return tags.toArray(new String[0]);
	}

	public RealImage getMainImage() {
		return mainImage;
	}

	public RealImage[] getSecondaryImages() {
		return secondaryImages.toArray(new RealImage[0]);
	}

	public double getPrice() {
		return price;
	}

	public CommentImpl[] getComments() {
		return Collections.unmodifiableList(comments).toArray(new CommentImpl[0]);
	}

	public void addComment(CommentImpl comment) {
		comments.add(comment);
	}

	public void addSecondaryImages(List<RealImage> images) {
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
