package application.controllers.home_screen;

import static application.utils.Animations.transitionX;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskLauncher;
import application.controllers.RemoteTaskObserver;
import application.controllers.library_screen.LibraryScreen;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BurgerMenuModule implements Initializable, Module, RemoteTaskObserver {
	public static final double BURGER_WIDTH = 220;

	@FXML
	private Pane paneRoot;
	@FXML
	private Button burgerButton;

	@FXML
	private Text firstName;
	@FXML
	private Text lastName;

	/* Buttons */
	@FXML
	private Button homeButton;
	@FXML
	private Button pendingButton;
	@FXML
	private Button booksButton;
	@FXML
	private Button addBookButton;
	@FXML
	private Button logOutButton;
	@FXML
	private Text logOutText;

	/* Whether the menu is hidden or not */
	private boolean isHidden = true;

	/* Module to add a new Book */
	private AddBookModule addBookModule;

	private ProxyModel proxyModel;

	private enum State {
		LAUNCHING_HOME, LAUNCHING_LIBRARY, DISCONNECTING, HIDLE;
	}

	private State state = State.HIDLE;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paneRoot.setTranslateX(-BURGER_WIDTH);
		loadFontsWorkaround();
	}

	@Override
	public void onLibraryRefreshed() {
		// Launching the home screen when the library is refreshed. This is the
		// only thing wanted by this menu here
		switch (state) {
		case DISCONNECTING:
			break;
		case HIDLE:
			break;
		case LAUNCHING_HOME:
			HomeScreen homeScreen = ModuleLoader.getInstance().load(HomeScreen.class);
			ClientMLVSchool.getINSTANCE().setInstantNewScreen(homeScreen);
			homeScreen.startHasMainScreen();
			break;
		case LAUNCHING_LIBRARY:
			LibraryScreen libraryScreen = ModuleLoader.getInstance().load(LibraryScreen.class);
			ClientMLVSchool.getINSTANCE().setInstantNewScreen(libraryScreen);
			break;
		}
		state = State.HIDLE;
	}

	@FXML
	public void homeClicked() {
		if (state != State.HIDLE) {
			return;
		}
		RemoteTaskLauncher.refreshLibrary(proxyModel, this);
		state = State.LAUNCHING_HOME;
	}

	@FXML
	public void libraryClicked() {
		if (state != State.HIDLE) {
			return;
		}
		RemoteTaskLauncher.refreshLibrary(proxyModel, this);
		state = State.LAUNCHING_LIBRARY;
	}

	@FXML
	public void profilClicked() {
		if (state != State.HIDLE) {
			return;
		}
		RemoteTaskLauncher.refreshLibrary(proxyModel, this);
		state = State.LAUNCHING_LIBRARY;
	}

	@FXML
	public void addBookClicked() {
		addBookModule.show();
		hide();
	}

	@FXML
	public void logOutClicked() {
		if (state != State.HIDLE) {
			return;
		}
		RemoteTaskLauncher.disconnect(proxyModel);
		state = State.DISCONNECTING;
	}

	@FXML
	public void burgerClicked() {
		if (isHidden) {
			show();
			return;
		}
		hide();
	}

	@Override
	public void hide() {
		if (isHidden) {
			return;
		}
		TranslateTransition transitionX = transitionX(0, -BURGER_WIDTH, 450, Interpolator.EASE_OUT, paneRoot);
		transitionX.setOnFinished(e -> isHidden = true);
		transitionX.play();
	}

	@Override
	public void show() {
		if (!isHidden) {
			return;
		}
		TranslateTransition transitionX = transitionX(-BURGER_WIDTH, 0, 450, Interpolator.EASE_OUT, paneRoot);
		transitionX.setOnFinished(e -> isHidden = false);
		transitionX.play();
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	private void loadFontsWorkaround() {
		// FontManager.
		FontManager fontManager = FontManager.getInstance();
		Font font1 = fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 32);
		Font font2 = fontManager.getFont(Constants.SF_TEXT_REGULAR, 28);
		Font font3 = fontManager.getFont(Constants.SF_DISPLAY_THIN, 28);
		homeButton.setFont(font1);
		pendingButton.setFont(font1);
		booksButton.setFont(font1);
		addBookButton.setFont(font1);
		logOutText.setFont(font2);
		firstName.setFont(font3);
		lastName.setFont(font3);
	}

	public void setUserInfo(String firstName, String lastName) {
		this.firstName.setText(firstName);
		this.lastName.setText(lastName);
	}

	public void setAddBookModule(AddBookModule addBookModule) {
		this.addBookModule = Objects.requireNonNull(addBookModule);
	}

	public void setUserInfo(ProxyModel proxyModel) {
		this.proxyModel = proxyModel;
		UserAsynchrone user = proxyModel.getConnectedUser();
		this.firstName.setText(user.getFirstName());
		this.lastName.setText(user.getLastName());
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
