package utils;

import java.rmi.RemoteException;
import java.util.ArrayList;

import fr.upem.rmirest.bilmancamp.helpers.ImageHelper;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class Mapper {

	/**
	 * Map a {@link Book} to a {@link BookPOJO}
	 * 
	 * @param book
	 *            the {@link Book} to map
	 * @return the mapped {@link BookPOJO}
	 * @throws RemoteException
	 */
	public static BookPOJO createBookPOJO(Book book) throws RemoteException {

		return new BookPOJO(book.getId(), book.getTitle(), book.getAuthors(), book.getSummary(), book.getCategories(),
				book.getPrice(), book.getTags(), ImageHelper.createRealImage((book.getMainImage())));
	}

	/**
	 * Map a {@link User} to a {@link UserPOJO}
	 * 
	 * @param user
	 *            the {@link User} to map
	 * @return the mapped {@link UserPOJO}
	 * @throws RemoteException
	 */
	public static UserPOJO createUserPOJO(User user) throws RemoteException {

		return new UserPOJO(user.getId(), user.getStatus(), user.getFirstName(), user.getLastName(), "",
				user.getCardNumber(), new ArrayList<>());
	}
}
