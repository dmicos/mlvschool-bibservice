package application.controllers.connection_screen;

import java.util.List;

import application.ClientMLVSchool;
import application.model.ProxyModel;
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
class ConnectionScreenRemoteTaskLauncher {

	/**
	 * Connection timeout to reach the server again. (Each Timeout).
	 */
	private static final int TIMEOUT = 1000;

	static void connectServer(ConnectionScreen screen) {

		ProxyModel proxyModel = screen.getProxyModel();
		Task<List<String>> task = new Task<List<String>>() {
			@Override
			protected List<String> call() throws Exception {
				proxyModel.connectServer();
				return proxyModel.getLibraryStatus();
			}
		};

		// Handling the success.
		task.setOnSucceeded(e -> screen.onServerReached(task.getValue()));

		// Handling the failure.
		task.setOnFailed(e -> {
			System.err.println("Server not reached.");
			PauseTransition p = new PauseTransition(new Duration(TIMEOUT));
			System.err.println("Connecting again.");
			p.setOnFinished(ee -> connectServer(screen));
			p.play();
		});

		launchTask(task);
	}

	static void connectUser(ConnectionScreen screen, LogInModule loginModule, String login, String password) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<ConnectingTaskInfo> task = createConnectingUserTaskWithSucceedHandled(screen, login, password, proxyModel);

		// Handling the failure.
		task.setOnFailed(e -> {
			System.err.println("Login failed.");
			if (task.getException().getCause().getClass() == IllegalArgumentException.class) {
				loginModule.onLoginError();
				return;
			}
			reloadApplication("Connection lost");
		});

		launchTask(task);
	}

	static void connectUserAfterAddInLibrary(ConnectionScreen screen, String login, String password) {
		ProxyModel proxyModel = screen.getProxyModel();

		Task<ConnectingTaskInfo> task = createConnectingUserTaskWithSucceedHandled(screen, login, password, proxyModel);

		// Handling the failure.
		task.setOnFailed(e -> reloadApplication("Critic login failed. After account creation"));

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
				// TODO use notification for this.
				System.err.println("User created");
				screen.onUserAdded(firstName, lastName, cardID, password, status);
				return;
			}
			// Not connected with no exceptions.
			signUpModule.onSignUpFailed();
		});

		// Handling the failure.
		task.setOnFailed(e -> {
			reloadApplication("Adding user to the Library failed.");
		});

		launchTask(task);
	}

	private static void reloadApplication(String message) {
		// TODO cancel all pending tasks if there are.
		// TODO notification "An error has occurred while transmitting with
		// the server."
		System.err.println(message);
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
			System.err.println("Login succeed.");
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
