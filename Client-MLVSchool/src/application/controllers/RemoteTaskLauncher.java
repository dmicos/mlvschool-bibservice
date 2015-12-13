package application.controllers;

import java.util.List;
import java.util.Objects;

import application.ClientMLVSchool;
import application.controllers.connection_screen.ConnectionScreen;
import application.controllers.connection_screen.LogInModule;
import application.controllers.connection_screen.SignUpModule;
import application.controllers.home_screen.AddBookModule;
import application.controllers.research_screen.ResearchScreen;
import application.model.BookAsynchrone;
import application.model.LibraryAsynchrone;
import application.model.ModelRules;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 * Launches thread safe tasks on the Application UI. Every code written here is
 * executed/reached in the JavaFX Thread.
 * 
 * @author Jefferson
 *
 */
public class RemoteTaskLauncher {

	/**
	 * Connection timeout to reach the server again. (Each Timeout).
	 */
	private static final int TIMEOUT = 1000;

	public static void connectServer(ConnectionScreen screen) {

		ProxyModel proxyModel = screen.getProxyModel();
		Task<LibraryAsynchrone> task = new Task<LibraryAsynchrone>() {
			@Override
			protected LibraryAsynchrone call() throws Exception {
				proxyModel.connectServer();
				return proxyModel.getLibrary();
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> screen.onServerReached(task.getValue()));

		// Handling the failure.
		task.setOnFailed(e -> {
			// Server not reached. Connecting again.
			Throwable exception = task.getException();
			System.err.println(exception.getCause() + "\n" + exception.getMessage());
			PauseTransition p = new PauseTransition(new Duration(TIMEOUT));
			p.setOnFinished(ee -> connectServer(screen));
			p.play();
		});

		launchTask(task);
	}

	public static void connectUser(ConnectionScreen screen, LogInModule loginModule, String login, String password) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<ConnectingTaskInfo> task = createConnectingUserTaskWithSucceedHandled(screen, login, password, proxyModel);
		// Handling the failure.
		task.setOnFailed(e -> {
			Throwable exception = task.getException();
			if (exception == null) {
				// ClientMLVSchool.reloadApplicationFirstScreen();
				reloadApplication("Connection lost.", task.getException());
				return;
			}
			if (exception.getClass() == IllegalArgumentException.class) {
				loginModule.onLoginError();
				return;
			}
			reloadApplication("Connection lost.", task.getException());
		});

		launchTask(task);
	}

	public static void connectUserAfterAddInLibrary(ConnectionScreen screen, String login, String password) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<ConnectingTaskInfo> task = createConnectingUserTaskWithSucceedHandled(screen, login, password, proxyModel);

		// Handling the failure.
		task.setOnFailed(e -> reloadApplication(
				"Error of transmision with the Library : " + task.getException().getMessage(), task.getException()));

		launchTask(task);
	}

	public static void addUser(ConnectionScreen screen, SignUpModule signUpModule, String firstName, String lastName,
			int cardID, String password, String status) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.addUser(firstName, lastName, cardID, password, status);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			if (task.getValue()) {
				String title = status + " created !";
				String message = "Welcome to your new library " + firstName + ' ' + lastName;
				NotificationsManager.notify(title, message, NotificationType.INFO);
				screen.onUserAdded(firstName, lastName, cardID, password, status);
				return;
			}
			// Not connected with no exceptions.
			String message = ModelRules.computeUserLogging(lastName, cardID) + " already exists.";
			NotificationsManager.notify("Information :", message, NotificationType.INFO);
			signUpModule.onSignUpFailed();
		});

		// Handling the failure.
		task.setOnFailed(e -> {
			reloadApplication("Adding user to the Library failed.", task.getException());
		});

		launchTask(task);
	}

	public static void addBook(Screen screen, AddBookModule addBookModule, String title, List<String> authors,
			String summary, String mainImage, List<String> secondaryImages, List<String> categories, double price,
			List<String> tags) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.addBook(title, authors, summary, mainImage, secondaryImages, categories, price, tags);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			if (task.getValue()) {
				addBookModule.onBookAdded(title);
			} else {
				addBookModule.onBookAddedError();
			}
		});

		// Handling the failure.
		task.setOnFailed(e -> {
			Throwable exception = task.getException();
			if (exception == null) {
				reloadApplication("Connection lost.", task.getException());
				return;
			}
			if (exception.getClass() == IllegalArgumentException.class) {
				addBookModule.onBookAddedError();
				return;
			}
			reloadApplication("Connection lost.", task.getException());
		});

		launchTask(task);
	}

	public static void searchBooksByCategory(Screen screen, String category) {
		ProxyModel proxyModel = screen.getProxyModel();
		Task<List<BookAsynchrone>> task = new Task<List<BookAsynchrone>>() {
			@Override
			protected List<BookAsynchrone> call() throws Exception {
				return proxyModel.searchByCategory(category);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			// Creating the research screen.
			ResearchScreen researchScreen = ModuleLoader.getInstance().load(ResearchScreen.class);
			ClientMLVSchool.getINSTANCE().setInstantNewScreen(researchScreen);
			researchScreen.loadBooks(category, task.getValue());
		});

		// Handling the failure.
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void refreshLibrary(ProxyModel proxyModel, RemoteTaskObserver observer) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				proxyModel.reloadLibrary();
				return null;
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> observer.onLibraryRefreshed());

		// Handling the failure.
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void searchBooks(String[] keywords, ProxyModel proxyModel, RemoteTaskObserver searchModule) {
		Objects.requireNonNull(keywords);
		Objects.requireNonNull(proxyModel);
		Task<List<BookAsynchrone>> task = new Task<List<BookAsynchrone>>() {
			@Override
			protected List<BookAsynchrone> call() throws Exception {
				return proxyModel.search(keywords);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> searchModule.onBookFound(task.getValue(), keywords));
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void disconnect(ProxyModel proxyModel) {
		Objects.requireNonNull(proxyModel);
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				proxyModel.disconnectUser();
				return null;
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> disconnect());
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void updateBookToVisualize(BookAsynchrone book, RemoteTaskObserver observer) {
		Objects.requireNonNull(book);
		ProxyModel proxyModel = Objects.requireNonNull(observer.getProxyModel());
		Task<BookAsynchrone> task = new Task<BookAsynchrone>() {
			@Override
			protected BookAsynchrone call() throws Exception {
				return proxyModel.updateBook(book);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> observer.onBookVisualized(task.getValue()));
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void borrowBook(ProxyModel proxyModel, UserAsynchrone user, BookAsynchrone book,
			RemoteTaskObserver... observers) {
		Objects.requireNonNull(proxyModel);
		Objects.requireNonNull(user);
		Objects.requireNonNull(book);
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.borrowBook(user, book);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			for (RemoteTaskObserver obs : observers) {
				obs.onBookBorrowed(task.getValue(), book, user);
			}
		});
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void giveBack(ProxyModel proxyModel, UserAsynchrone user, BookAsynchrone book,
			RemoteTaskObserver... observers) {
		Objects.requireNonNull(proxyModel);
		Objects.requireNonNull(user);
		Objects.requireNonNull(book);
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.giveBack(user, book);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			for (RemoteTaskObserver obs : observers) {
				obs.onBookGivenBack(task.getValue(), book, user);
			}
		});
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void cancel(ProxyModel proxyModel, UserAsynchrone user, BookAsynchrone book,
			RemoteTaskObserver... observers) {
		Objects.requireNonNull(proxyModel);
		Objects.requireNonNull(user);
		Objects.requireNonNull(book);
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.cancel(user, book);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			for (RemoteTaskObserver obs : observers) {
				obs.onBookCancel(task.getValue(), book, user);
			}
		});
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	public static void addComment(ProxyModel proxyModel, BookAsynchrone book, String content, int rate,
			RemoteTaskObserver obs) {
		Objects.requireNonNull(proxyModel);
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return proxyModel.addComment(book, content, rate);
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			obs.onCommentaryOnBookPosted(book);
		});
		task.setOnFailed(e -> reloadApplication("Connection lost.", task.getException()));
		launchTask(task);
	}

	private static void reloadApplication(String message, Throwable throwable) {
		NotificationsManager.notify("Library :", message, NotificationType.DATABASE);
		ClientMLVSchool.getINSTANCE().reloadApplicationFirstScreen();
		System.err.println(throwable.getMessage());
		throwable.printStackTrace();
	}

	private static void disconnect() {
		NotificationsManager.notify("Library :", "Good bye !", NotificationType.INFO);
		ClientMLVSchool.getINSTANCE().reloadApplicationFirstScreen();
	}

	private static void launchTask(Task<?> task) {
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private static Task<ConnectingTaskInfo> createConnectingUserTaskWithSucceedHandled(ConnectionScreen screen,
			String login, String password, ProxyModel proxyModel) {
		Task<ConnectingTaskInfo> task = new Task<ConnectingTaskInfo>() {
			@Override
			protected ConnectingTaskInfo call() throws Exception {
				User user = proxyModel.connectUser(login, password);
				return new ConnectingTaskInfo(user, user.getFirstName(), user.getLastName());
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> {
			ConnectingTaskInfo info = task.getValue();
			screen.onUserConnected(info.user, info.firstName, info.lastName);
		});
		return task;
	}

	private static class ConnectingTaskInfo {
		final User user;
		final String firstName;
		final String lastName;

		public ConnectingTaskInfo(User user, String firstName, String lastName) {
			this.user = user;
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}
}
