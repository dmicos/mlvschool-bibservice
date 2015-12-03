package fr.upem.rmirest.bilmancamp.interfaces;


/**
 * Warning ! Server security : All User's methods invoked here must not throw
 * any exceptions.
 *
 */
public interface Book {

	// Getters de base du livre.
	// TODO ajouter les getters.

	// Modification methods.
	public boolean borrow(User user);

	public void rate(User user);

	public void comment(BookComment bookComment);

	public void giveBack(User user);

	public void unregister(User user);

	public int getRankInWaitingQueue(User user);
}
