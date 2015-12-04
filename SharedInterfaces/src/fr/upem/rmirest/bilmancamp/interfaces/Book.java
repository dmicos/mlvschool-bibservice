package fr.upem.rmirest.bilmancamp.interfaces;

/**
 * Warning ! Server security : All User's methods invoked here must not throw
 * any exceptions.
 *
 */
public interface Book {

	// Getters de base du livre.
	// TODO ajouter les getters.

	/**
	 * Borrows the current book if available. If it is not, add the user in the
	 * reservation queue.
	 * 
	 * @param user
	 *            the {@link User} which want to borrow the current book.
	 * @return <code>true</code> if the current book was available ;
	 *         <code>false</code> otherwise and if the user was added to the
	 *         reservation queue.
	 */
	public boolean borrow(User user);

	/**
	 * Allows the {@link User} to rate the current book.
	 * 
	 * @param user
	 *            the {@link User} who give the mark.
	 * @param evaluation
	 *            the evaluation of the user.
	 * @throws IllegalArgumentException
	 *             is the evaluation is not in the correct range. [0, 5].
	 */
	public void rate(User user, int evaluation) throws IllegalArgumentException;

	/**
	 * Adds a commentary to the current {@link Book}.
	 * 
	 * @param bookComment
	 *            the commentary about the current book.
	 */
	public void comment(BookComment bookComment);

	/**
	 * Allow the given {@link User} to give back a book if he borrowed it.
	 * 
	 * @param user
	 *            the {@link User} who give back the current {@link Book}.
	 */
	public void giveBack(User user);

	/**
	 * Unregister the given {@link User} from the reservation queue of the
	 * current {@link Book}.
	 * 
	 * @param user
	 *            the {@link User} who unregister the reservation queue.
	 */
	public void unregister(User user);

	/**
	 * Allow the given {@link User} to know it's position into the reservation
	 * queue of the current {@link Book}.
	 * 
	 * @param user
	 *            the {@link User} who wants to know it's position in the
	 *            current {@link Book}'s reservation queue.
	 * @return the position of the given {@link User} in the current
	 *         {@link Book}'s reservation queue.
	 */
	public int getRankInWaitingQueue(User user);
}
