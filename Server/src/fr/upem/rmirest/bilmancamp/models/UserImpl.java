package fr.upem.rmirest.bilmancamp.models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fr.upem.rmirest.bilmancamp.helpers.UserHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Library;
import fr.upem.rmirest.bilmancamp.interfaces.User;

/**
 * {@link User} basic implementation.
 *
 * @author qcampos
 *
 */
public class UserImpl extends UnicastRemoteObject implements User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2857973181293273060L;

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
		private UserPOJO model;

		public RealUser(UserPOJO model) {
			this.model = model;
		}

		@Override
		public int getId() {
			return model.getId();
		}

		@Override
		public String getStatus() {
			return model.getStatus();
		}

		@Override
		public String getFirstName() {
			return model.getFirstName();
		}

		@Override
		public String getLastName() {
			return model.getLastName();
		}

		@Override
		public int getCardNumber() {
			return model.getCardNumber();
		}

		@Override
		public boolean isLoginValid(String id, String password) throws RemoteException {
			return id.equals(UserHelper.computeId(model)) && password.equals(model.getPassword());
		}

		@Override
		public void disconnect() {
			// Do nothing here.
		}

		@Override
		public String toString() {
			return String.format("%s n�%s - %s %s", getStatus(), getCardNumber(), getFirstName(), getLastName());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + model.getCardNumber();
			result = prime * result + ((model.getFirstName() == null) ? 0 : model.getFirstName().hashCode());
			result = prime * result + ((model.getLastName() == null) ? 0 : model.getLastName().hashCode());
			result = prime * result + ((model.getStatus() == null) ? 0 : model.getStatus().hashCode());
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

		@Override
		public boolean equals(Object obj) {
			return false;
		};

		@Override
		public int hashCode() {
			return 0;
		}
	};

	// Model fields
	/**
	 * The real {@link User} object which will receive all delegation methods.
	 */
	private User realUser;
	private UserPOJO model;

	public UserImpl(UserPOJO model) throws RemoteException {
		super();
		this.model = model;
		realUser = new RealUser(model);
	}

	@Override
	public int getId() throws RemoteException {
		return realUser.getId();
	}

	// Delegation methods.
	@Override
	public String getStatus() throws RemoteException {
		return realUser.getStatus();
	}

	@Override
	public String getFirstName() throws RemoteException {
		return realUser.getFirstName();
	}

	@Override
	public String getLastName() throws RemoteException {
		return realUser.getLastName();
	}

	@Override
	public int getCardNumber() throws RemoteException {
		return realUser.getCardNumber();
	}

	@Override
	public boolean isLoginValid(String id, String password) throws RemoteException {
		return realUser.isLoginValid(id, password);
	}

	@Override
	public void disconnect() throws RemoteException {
		// Change the real user to the null user, in order to keep the current
		// object valid even after being disconnected.
		realUser = NULL_USER_SINGLETON;

	}

	@Override
	public String toString() {
		return realUser.toString();
	}

	@Override
	public int hashCode() {
		// Card number is supposed to be unique.
		return realUser.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof UserImpl))
			return false;

		UserImpl other = (UserImpl) obj;

		return model.getId() == other.model.getId();
	}

}
