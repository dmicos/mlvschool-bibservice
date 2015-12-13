package fr.upem.rmirest.bilmancamp.modelloaders;

import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class UserWrapper {

	private int id;
	private String status;
	private String firstName;
	private String lastName;
	private String password;
	private int cardNumber;

	public UserWrapper() {
		// TODO Auto-generated constructor stub
	}

	public UserWrapper(int id, String status, String firstName, String lastName, String password, int cardNumber) {
		super();
		this.id = id;
		this.status = status;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.cardNumber = cardNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public UserPOJO toUserPOJO() {
		return new UserPOJO(id, status, firstName, lastName, password, cardNumber);
	}

}
