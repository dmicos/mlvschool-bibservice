package fr.upem.rmirest.bilmancamp.models;

import java.util.Collections;
import java.util.List;

import fr.upem.rmirest.bilmancamp.helpers.UserHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.User;

/**
 * {@link User} basic implementation.
 *
 * @author qcampos
 *
 */
public class UserImpl implements User {

	/**
	 * The {@link UserImpl} is in reality a proxy for {@link User}. It will
	 * delegate every operations to the class {@link RealUser}.
	 * 
	 * Proxy Pattern is used to switch from a real and connected {@link User} to
	 * a disconnected {@link User} which use a Null Object pattern to avoid
	 * issues when the client uses a disconnected user like a connected one.
	 * 
	 * @author qcampos
	 *
	 */
	private class RealUser implements User {

		// Model fields
		private final String status;
		private final String firstName;
		private final String lastName;
		private final String password;
		private final int cardNumber;

		private final List<Book> history;

		public RealUser(String status, String firstName, String lastName, String password, int cardNumber,
				List<Book> history) {

			// Model fields
			this.status = status;
			this.firstName = firstName;
			this.lastName = lastName;
			this.password = password;
			this.cardNumber = cardNumber;
			this.history = history;
		}

		@Override
		public int getId() {
			return 0;
		}

		@Override
		public String getStatus() {
			return status;
		}

		@Override
		public String getFirstName() {
			return firstName;
		}

		@Override
		public String getLastName() {
			return lastName;
		}

		@Override
		public int getCardNumber() {
			return cardNumber;
		}

		@Override
		public List<Book> getBookHistory() {
			return Collections.unmodifiableList(history);
		}

		@Override
		public boolean isLoginValid(String id, String password) {
			return id.equals(UserHelper.computeId(this)) && password.equals(this.password);
		}

		@Override
		public void disconnect() {
			// Do nothing here.
		}

		@Override
		public String toString() {
			return String.format("%s n°%s - %s %s", getStatus(), getCardNumber(), getFirstName(), getLastName());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + cardNumber;
			result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
			result = prime * result + ((status == null) ? 0 : status.hashCode());
			return result;
		}

		private UserImpl getOuterType() {
			return UserImpl.this;
		}

	}

	/**
	 * Null Object Pattern to avoid the client to have a invalid {@link User}
	 * after being disconnected. The invalid client could be used as an argument
	 * to the {@link Library} object, thing that we don't want at all.
	 */
	private final static User NULL_USER_SINGLETON = new User() {

		@Override
		public int getId() {
			return 0;
		};

		@Override
		public String getStatus() {
			return "None";
		}

		@Override
		public String getLastName() {
			return "None";
		}

		@Override
		public String getFirstName() {
			return "None";
		}

		@Override
		public int getCardNumber() {
			return 0;
		}

		@Override
		public List<Book> getBookHistory() {
			return Collections.emptyList();
		}

		@Override
		public void disconnect() {
			// Do nothing
		}

		@Override
		public boolean isLoginValid(String id, String password) {
			return false;
		}

		@Override
		public String toString() {
			return "Disconnected user";
		}

	};

	// Database fields
	private static int idCount = 1;
	private int id;

	// Model fields
	/**
	 * The real {@link User} object which will receive all delegation methods.
	 */
	private User realUser;

	public UserImpl(int id, String status, String firstName, String lastName, String password, int cardNumber,
			List<Book> history) {

		// Model field
		realUser = new RealUser(status, firstName, lastName, password, cardNumber, history);
		// Database fields
		this.id = id;
		idCount++;
	}

	public UserImpl(String status, String firstName, String lastName, String password, int cardNumber,
			List<Book> history) {
		this(idCount, status, firstName, lastName, password, cardNumber, Collections.emptyList());
	}

	public UserImpl(int id, String status, String firstName, String lastName, String password, int cardNumber) {
		this(id, status, firstName, lastName, password, cardNumber, Collections.emptyList());
	}

	@Override
	public int getId() {
		return id;
	}

	// Delegation methods.
	@Override
	public String getStatus() {
		return realUser.getStatus();
	}

	@Override
	public String getFirstName() {
		return realUser.getFirstName();
	}

	@Override
	public String getLastName() {
		return realUser.getLastName();
	}

	@Override
	public int getCardNumber() {
		return realUser.getCardNumber();
	}

	@Override
	public boolean isLoginValid(String id, String password) {
		return realUser.isLoginValid(id, password);
	}

	@Override
	public void disconnect() {
		// Change the real user to the null user, in order to keep the current
		// object valid even after being disconnected.
		realUser = NULL_USER_SINGLETON;

	}

	@Override
	public List<Book> getBookHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return realUser.toString();
	}

	@Override
	public int hashCode() {
		// Card number is supposed to be unique.
		return realUser.getCardNumber();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof User))
			return false;

		User other = (User) obj;

		return getId() == other.getId();
	}

}
