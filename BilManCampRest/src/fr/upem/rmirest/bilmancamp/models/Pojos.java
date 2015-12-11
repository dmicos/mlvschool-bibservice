package fr.upem.rmirest.bilmancamp.models;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import fr.upem.rmirest.bilmancamp.interfaces.Book;

public class Pojos {

	/**
	 * Converts a list of BookPojo into a list of Books (Remote).
	 * 
	 * @param books
	 * @return the list of book remote.
	 * @throws RemoteException
	 *             if any {@link RemoteException} occured during the mapping
	 *             conversion.
	 */
	public static List<Book> booksPojoToBooksRemote(List<BookPOJO> books) throws RemoteException {
		try {
			return books.stream().map(b -> {
				try {
					return new BookImpl(b);
				} catch (RemoteException e) {
					throw new UncheckedRemoteException(e);
				}
			}).collect(Collectors.toList());
		} catch (UncheckedRemoteException e) {
			throw e.getCause();
		}
	}
}
