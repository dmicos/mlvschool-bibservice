package fr.upem.rmirest.bilmancamp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.upem.rmirest.bilmancamp.interfaces.Book;

public class UserPOJO  implements Serializable{

	private static final long serialVersionUID = 1L;
	private final String status;
	private final String firstName;
	private final String lastName;
	private final String password;
	private final int cardNumber;
	private final List<Book> history;
	private final int id;

	public UserPOJO(int id, String status, String firstName, String lastName, String password, int cardNumber,
			List<Book> history) {
		super();
		this.status = status;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.cardNumber = cardNumber;
		this.history = history;
		this.id = id;
	}

	public UserPOJO(int id, String status, String firstName, String lastName, String password, int cardNumber) {
		this(id, status, firstName, lastName, password, cardNumber, new ArrayList<>());
	}

	public String getStatus() {
		return status;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	protected String getPassword() {
		return password;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public List<Book> getHistory() {
		return history;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof UserPOJO))
			return false;
		
		UserPOJO pojo=(UserPOJO)obj;
		return id == pojo.id;
	}
}
