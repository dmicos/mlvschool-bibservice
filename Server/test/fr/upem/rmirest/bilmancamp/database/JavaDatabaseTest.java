package fr.upem.rmirest.bilmancamp.database;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class JavaDatabaseTest {

	/**
	 * Return a {@link Database} of the tested implementation. Allows this junit
	 * test to be easily changed for every implementation.
	 * 
	 * @return a Database object of the tested class.
	 */
	private static Database implementation() {

		// try {
		// return new
		// EmbeddedDB(DBHelper.connect("jdbc:h2:./Server/rsc/library", "pony",
		// "merens*30"));
		// } catch (SQLException | ClassNotFoundException ex) {
		// return new JavaDatabase();
		// }
		// =======
		return new JavaDatabase();
	}

	private static Image image() {
		return new Image() {
			private static final long serialVersionUID = 4060559163469041882L;

			@Override
			public String getData() {
				// TODO Auto-generated method stub
				return "false path";
			}
		};
	}

	@Test
	public void testAddBook() throws RemoteException {
		// This should work and not throw any exception
		Database db = implementation();
		BookPOJO book = new BookPOJO(0, "Les aventures de champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42,
				Arrays.asList("Champimou", "saga"), image(), Arrays.asList(image()));
		db.addBook(book);
	}

	@Test
	public void testAddUser() throws RemoteException {
		// Standard addition to the database
		Database db = implementation();
		UserPOJO user = new UserPOJO(0, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		Assert.assertTrue("The user should have been added successfully", db.addUser(user));
	}

	@Test
	public void testAddUserExisting() throws RemoteException {
		Database db = implementation();
		UserPOJO user1 = new UserPOJO(0, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		UserPOJO user2 = new UserPOJO(1, "Student", "Joshua", "Mangue", "ilovequadrillage", 1, new ArrayList<>());
		db.addUser(user1, "pony");
		// User2 should have the same identifier.
		Assert.assertFalse("Another user with the same id should not be added to the database",
				db.addUser(user2, "ilovequadrillage"));
	}

	// @Test
	// public void testConnectUser() {
	// Database db = implementation();
	// User user1 = new UserImpl("Student", "Jefferson", "Mangue", "pony", 1,
	// new ArrayList<>());
	// db.addUser(user1, "pony");
	// // Assert.assertSame("Connexion with correct credentials should return
	// // the user", user1.getId(),
	// // db.connectUser("jmangue1", "pony").getId());
	//
	// }

	@Test
	public void testConnectUserWrongUsername() throws RemoteException {
		Database db = implementation();
		UserPOJO user1 = new UserPOJO(1, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		db.addUser(user1);
		Assert.assertNull("Connexion with a inexistant username should return null",
				db.connectUser("jmangue5", "pony"));
	}

	@Test
	public void testConnectUserWrongPassword() throws RemoteException {
		Database db = implementation();
		UserPOJO user1 = new UserPOJO(1, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		db.addUser(user1);
		Assert.assertNull("Connexion with a wrong password should return null.", db.connectUser("jmangue1", "ponies"));
	}

	@Test
	public void testSearchBookFromKeywords() throws RemoteException {
		Database db = implementation();
		BookPOJO book1 = new BookPOJO(0, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO book2 = new BookPOJO(1, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO anotherbook = new BookPOJO(2, "Dive into Python", Arrays.asList("JYT"), "A guide for python 3",
				Arrays.asList("Programming"), 666.42, Arrays.asList("Software", "python"), image(),
				Arrays.asList(image()));
		db.addBook(book1);
		db.addBook(book2);

		List<BookPOJO> result = db.searchBookFromKeywords("champimou", "champidur");
		// The research contains the books about champi*.

		Assert.assertTrue("Book1 not in result", result.contains(book1));
		Assert.assertTrue("Book2 not in result", result.contains(book2));
		// A book non-related to the keywords is not found.
		Assert.assertFalse("A book with no match is in the list", result.contains(anotherbook));
	}

	@Test
	public void testGetCategories() throws RemoteException {
		Database db = implementation();
		assert(null != db.getCategories());
		// fail("DataBase interface doesn't yet allow this kind of test."); //
		// TODO

	}

	@Test
	public void testGetBookFromCategory() throws RemoteException {
		Database db = implementation();
		BookPOJO book1 = new BookPOJO(0, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO book2 = new BookPOJO(1, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		db.addBook(book1);
		db.addBook(book2);

		List<BookPOJO> result = db.getBookFromCategory("sf");
		// Check the result of this request.
		Assert.assertFalse("Contains a non SF book", result.contains(book1));
		Assert.assertTrue("Not contains the searched SF book", result.contains(book2));
	}

	@Test
	public void testGetBookRecents() throws RemoteException {
		Database db = implementation();
		BookPOJO book1 = new BookPOJO(0, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO book2 = new BookPOJO(1, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		db.addBook(book1);
		db.addBook(book2);
		BookPOJO lastBook = new BookPOJO(1, "Dive into Python", Arrays.asList("JYT"), "A guide for python 3",
				Arrays.asList("Programming"), 666.42, Arrays.asList("Software", "python"), image(),
				Arrays.asList(image()));
		// Do not db.addBook(book2);
		db.addBook(lastBook);

		// Get the last added book.
		List<BookPOJO> result = db.getBookRecents(1);
		// Check if it is only the last one.
		Assert.assertTrue("Last added book is not in the list", result.contains(lastBook));
		Assert.assertFalse("An older book is in the list", result.contains(book1));
		Assert.assertFalse("A book never added to the database is in the list", result.contains(book2));
	}

	@Test
	public void testGetBookBestRate() throws RemoteException {
		fail("Need some improvements of the DataBase interface.");
		Database db = implementation();
		BookPOJO book1 = new BookPOJO(0, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO book2 = new BookPOJO(1, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		db.addBook(book1);
		db.addBook(book2);
		// Create user.
		UserPOJO user = new UserPOJO(2, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		db.addUser(user);
		// Rate a book.
		/*
		 * TODO book1.rate(user, 5); book2.rate(user, 4);
		 */
		// Get THE best rated book.
		List<BookPOJO> result = db.getBookBestRate(1);
		Assert.assertTrue("Best rated book is not in the list", result.contains(book1));
		Assert.assertFalse("Less rated book is in the list", result.contains(book2));
	}

	@Test
	public void testGetBookMostConsulted() throws RemoteException {
		fail("Need some improvements of the DataBase interface.");
		Database db = implementation();
		// Add books.
		BookPOJO book1 = new BookPOJO(1, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("Romance"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		BookPOJO book2 = new BookPOJO(2, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		db.addBook(book1);
		db.addBook(book2);
		// Create user.
		UserPOJO user = new UserPOJO(1, "Student", "Jefferson", "Mangue", "pony", 1, new ArrayList<>());
		db.addUser(user);
		// Borrow some books
		/*
		 * TODO book1.borrow(user); book1.giveBack(user); book1.borrow(user);
		 * book2.borrow(user);
		 */
		// Get the most borrowed book
		List<BookPOJO> result = db.getBookMostConsulted(1);
		// Check if only the most is in the list.
		Assert.assertTrue("Most consulted book is not in the list", result.contains(book1));
		Assert.assertFalse("Less consulted book is in the list", result.contains(book2));
	}

	@Test
	public void testGetBookMostSimilar() throws RemoteException {
		assert(5 == 3);
		Database db = implementation();
		// Create and add some books
		BookPOJO refBook = new BookPOJO(0, "Les nouvelles aventures de Champidur", Arrays.asList("JYT"),
				"Un voyage vers des contrées nouvelles", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		// "les" "aventures", "de", "un", "sf", "saga" : 6
		BookPOJO book1 = new BookPOJO(1, "Les aventures de Champimou", Arrays.asList("Quentin"),
				"Un récit d'aventure exceptionnel", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		// "de", "sf", "saga" : 3
		BookPOJO book2 = new BookPOJO(0, "Le retour de ChampiMou", Arrays.asList("Yannerson"),
				"Le spectre du mal rôde à nouveau", Arrays.asList("SF"), 666.42, Arrays.asList("saga"), image(),
				Arrays.asList(image()));
		// No matches
		BookPOJO anotherbook = new BookPOJO(1, "Dive into Python", Arrays.asList("JYP"), "A guide for python 3",
				Arrays.asList("Programming"), 666.42, Arrays.asList("Software", "python"), image(),
				Arrays.asList(image()));

		db.addBook(refBook);
		db.addBook(book1);
		db.addBook(book2);
		db.addBook(anotherbook);
		// Do the request.
		List<BookPOJO> result = db.getBookMostSimilar(refBook, 5);

		// Check the order and the content of the result.
		Assert.assertFalse("Reference book should not be matched.", result.contains(refBook));
		Assert.assertTrue("Most similar book is not the first item", book1 == result.get(0));
		Assert.assertTrue("Less similar book is not the last", book2 == result.get(1));
		Assert.assertFalse("List of similar books contains a book with no matches", result.contains(anotherbook));
	}

}
