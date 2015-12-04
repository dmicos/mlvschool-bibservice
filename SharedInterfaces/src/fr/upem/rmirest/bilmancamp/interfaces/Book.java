package fr.upem.rmirest.bilmancamp.interfaces;

import java.util.List;

/**
 * Warning ! Server security : All User's methods invoked here must not throw
 * any exceptions.
 *
 */

public interface Book {

	// Description getters.
	String getTitle();

	List<String> getAuthors();

	List<String> getCategories();

	double getPrice();

	float getRate();

	int getRateNumber();

	List<String> getTags();

	Image getMainImage();

	List<Image> getSecondaryImages();

	// TODO ajouter les autres getters de je suis fatigué loool.
	// Tu les trouveras en faisant la classe d'implantation !
	// Je checkerai après mon cours,,,,

	// Modification methods.
	public boolean borrow(User user);

	public void rate(User user);

	public void comment(BookComment bookComment);

	public void giveBack(User user);

	public void unregister(User user);

	public int getRankInWaitingQueue(User user);
}
