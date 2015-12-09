package fr.upem.rmirest.bilmancamp.models;

/**
 * @author Jefferson
 */

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Wraps an {@link RemoteException} with an unchecked exception.
 *
 * @since 1.8
 */
public class UncheckedRemoteException extends RuntimeException {

	private static final long serialVersionUID = -8134305061645241065L;

	/**
	 * Constructs an instance of this class.
	 *
	 * @param message
	 *            the detail message, can be null
	 * @param cause
	 *            the {@code IOException}
	 *
	 * @throws NullPointerException
	 *             if the cause is {@code null}
	 */
	public UncheckedRemoteException(String message, RemoteException cause) {
		super(message, Objects.requireNonNull(cause));
	}

	/**
	 * Constructs an instance of this class.
	 *
	 * @param cause
	 *            the {@code IOException}
	 *
	 * @throws NullPointerException
	 *             if the cause is {@code null}
	 */
	public UncheckedRemoteException(RemoteException cause) {
		super(Objects.requireNonNull(cause));
	}

	/**
	 * Returns the cause of this exception.
	 *
	 * @return the {@code IOException} which is the cause of this exception.
	 */
	@Override
	public RemoteException getCause() {
		return (RemoteException) super.getCause();
	}

	/**
	 * Called to read the object from a stream.
	 *
	 * @throws InvalidObjectException
	 *             if the object is invalid or has a cause that is not an
	 *             {@code IOException}
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		Throwable cause = super.getCause();
		if (!(cause instanceof RemoteException))
			throw new InvalidObjectException("Cause must be an IOException");
	}

}
