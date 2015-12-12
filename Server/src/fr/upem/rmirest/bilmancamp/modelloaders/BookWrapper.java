package fr.upem.rmirest.bilmancamp.modelloaders;

import java.util.ArrayList;
import java.util.List;

import fr.upem.rmirest.bilmancamp.models.BookPOJO;

/**
 * Wrapper for a {@link BookPOJO} used for json serialization.
 * 
 * @author qcampos
 *
 */
public class BookWrapper {

	private int id;
	private String title;
	private List<String> authors;
	private String summary;
	private List<String> categories;
	private double price;
	private List<String> tags;
	private String mainImage;
	private List<String> secondaryImages;

	public BookWrapper() {
		super();
	}

	public BookWrapper(int id, String title, List<String> authors, String summary, List<String> categories,
			double price, List<String> tags, String mainImage, List<String> secondaryImages) {
		super();
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.summary = summary;
		this.categories = categories;
		this.price = price;
		this.tags = tags;
		this.mainImage = mainImage;
		this.secondaryImages = secondaryImages;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public List<String> getSecondaryImages() {
		return secondaryImages;
	}

	public void setSecondaryImages(List<String> secondaryImages) {
		this.secondaryImages = secondaryImages;
	}

	public BookPOJO toBookPOJO() {
		ArrayList<String> secImg = new ArrayList<>();
		secImg.addAll(secondaryImages);
		return new BookPOJO(id, title, authors, summary, categories, price, tags, mainImage, secImg);
	}

}