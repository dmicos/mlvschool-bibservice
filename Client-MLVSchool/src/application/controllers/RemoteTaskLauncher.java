package application.controllers;

import java.util.List;

import application.ClientMLVSchool;
import application.controllers.connection_screen.ConnectionScreen;
import application.controllers.connection_screen.LogInModule;
import application.controllers.connection_screen.SignUpModule;
import application.controllers.home_screen.AddBookModule;
import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.SearchModule;
import application.controllers.research_screen.ResearchScreen;
import application.model.BookAsynchrone;
import application.model.LibraryAsynchrone;
import application.model.ModelRules;
import application.model.ProxyModel;
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
				reloadApplication("Connection lost.");
				return;
			}
			if (exception.getClass() == IllegalArgumentException.class) {
				loginModule.onLoginError();
				return;
			}
			reloadApplication("Connection lost.");
		});

		launchTask(task);
	}

	public static void connectUserAfterAddInLibrary(ConnectionScreen screen, String login, String password) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<ConnectingTaskInfo> task = createConnectingUserTaskWithSucceedHandled(screen, login, password, proxyModel);

		// Handling the failure.
		task.setOnFailed(
				e -> reloadApplication("Error of transmision with the Library : " + task.getException().getMessage()));

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
			reloadApplication("Adding user to the Library failed.");
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
				// ClientMLVSchool.reloadApplicationFirstScreen();
				reloadApplication("Connection lost.");
				return;
			}
			if (exception.getClass() == IllegalArgumentException.class) {
				addBookModule.onBookAddedError();
				return;
			}
			reloadApplication("Connection lost.");
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
			// homeScreen.researchBooksReady(category, task.getValue());
			System.out.println("HomeScreen : Searching books : After");
			// Removing modules
			BurgerMenuModule burgerMenuModule = screen.getBurgerMenuModule();
			SearchModule searchModule = screen.getSearchModule();
			screen.getView().getChildren().remove(burgerMenuModule);
			screen.getView().getChildren().remove(searchModule);
			// Creating the research screen.
			ResearchScreen researchScreen = ModuleLoader.getInstance().load(ResearchScreen.class);
			researchScreen.initContent(proxyModel, category, task.getValue(), burgerMenuModule, searchModule);
			ClientMLVSchool.setInstantNewScreen(screen, researchScreen);
			System.out.println("HomeScreen : Searching books : After after");
		});

		// Handling the failure.
		task.setOnFailed(e -> reloadApplication("Connection lost."));
		launchTask(task);
	}

	private static void reloadApplication(String message) {
		NotificationsManager.notify("Library :", message, NotificationType.DATABASE);
		ClientMLVSchool.reloadApplicationFirstScreen();
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
