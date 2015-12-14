package fr.upem.rmirest.bilmancamp.modelloaders;

import java.util.ArrayList;

import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class RateWrapper {

	private final UserPOJO user;
	private final BookPOJO book;

	private final int value;

	public RateWrapper(int user, int book, int value) {

		this.user = new UserPOJO(user, "", "", "", "", 0);
		this.book = new BookPOJO(book, "", new ArrayList<>(), "", new ArrayList<>(), 0.0, new ArrayList<>(), "");

		this.value = value;
	}

	public UserPOJO getUser() {
		return user;
	}

	public BookPOJO getBook() {
		return book;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("User %s gave %d to book %s", user, value, book);
	}

}
