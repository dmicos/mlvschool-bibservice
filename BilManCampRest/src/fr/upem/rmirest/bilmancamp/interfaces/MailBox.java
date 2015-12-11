package fr.upem.rmirest.bilmancamp.interfaces;

import java.rmi.Remote;

/**
 * Represents a Callback address in which user will be contacted
 * 
 * @author ybilissor
 *
 * @param <T>
 */
public interface MailBox<T> extends Remote {

	/**
	 * Use to notify user of incoming message
	 * 
	 * @param value
	 *            The data to send
	 */
	public void receive(T value);
}
