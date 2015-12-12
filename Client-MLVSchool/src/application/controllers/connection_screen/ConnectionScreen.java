package application.controllers.connection_screen;

import static application.utils.Animations.transitionOpacityAnimation;
import static application.utils.Constants.SF_DISPLAY_LIGHT;
import static application.utils.Constants.SF_DISPLAY_REGULAR;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.controllers.home_screen.HomeScreen;
import application.model.LibraryAsynchrone;
import application.model.ModelRules;
import application.model.ProxyModel;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.FontManager;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Main controller of the Connection screen.
 * 
 * @author Jefferson
 *
 */
public class ConnectionScreen implements Initializable, Screen {

	@FXML
	private Pane paneRoot;

	/* Buttons */
	@FXML
	private Button buttonSignUp;
	@FXML
	private Button buttonLogIn;

	/* Title */
	@FXML
	private Text title;
	@FXML
	private Text subTitle;
	@FXML
	private VBox titleGroup;
	@FXML
	private Text footer;

	/* Shows messages about the current state : like "connecting"... */
	private Text stateMessagesText;

	/* Indicator of the pending connection to the library */
	private ProgressIndicator progressIndicator;

	/* SignUp module */
	private SignUpModule signUpController;

	/* LogIn module */
	private LogInModule logInController;

	/* Model controlled */
	private ProxyModel proxyModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Loading local coded objects.
		stateMessagesText = new Text();
		progressIndicator = new ProgressIndicator();
		stateMessagesText.setFill(new Color(1, 1, 1, 0.7f));
		paneRoot.getChildren().add(stateMessagesText);
		paneRoot.getChildren().add(progressIndicator);

		// Loading fonts of the current objects.
		loadFontsWorkaround();

		// Loading modules.
		signUpController = loadModule(SignUpModule.class);
		logInController = loadModule(LogInModule.class);
		signUpController.setConnectionScreenController(this);
		logInController.setConnectionScreenController(this);

		// Hiding every thing at first.
		hide();
	}

	public void startCient() {
		ConnectionScreenRemoteTaskLauncher.connectServer(this);
	}

	/**
	 * Sets the current PROXY model used to populate the screen.
	 */
	public void setModel(ProxyModel proxyModel) {
		this.proxyModel = Objects.requireNonNull(proxyModel);
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}

	/**
	 * Launches the animation title. Here the connection with the server has to
	 * be done.
	 */
	void onServerReached(LibraryAsynchrone library) {
		startTitleGroupAnimation();
		boolean appear = true, fromRight = true;
		buttonsTransition(() -> buttonLogIn.setDisable(false), () -> buttonSignUp.setDisable(false), appear, fromRight);
		progressIndicator.setVisible(false);
		stateMessagesText.setVisible(false);
		signUpController.setStatusList(library.getStatus());
	}

	/**
	 * Notifies the current {@link ConnectionScreen} that a user has been
	 * authenticated. Transition to the home screen with this account.
	 */
	public void onUserConnected(User user, String firstName, String lastName) {
		// Adding a new message of Welcome.
		Text welcomeText = new Text("Welcome, " + firstName + ' ' + lastName + '.');
		welcomeText.setFill(new Color(1, 1, 1, 0.7f));
		welcomeText.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_LIGHT, 24));
		welcomeText.setLayoutX((paneRoot.getPrefWidth() - welcomeText.maxWidth(-1)) / 2f);
		welcomeText.setLayoutY((paneRoot.getPrefHeight() - welcomeText.maxHeight(-1)) / 2f);
		welcomeText.setOpacity(0);
		welcomeText.setCache(true);
		welcomeText.setCacheHint(CacheHint.SPEED);
		paneRoot.getChildren().add(welcomeText);

		Animations.delay(800, transitionOpacityAnimation(Interpolator.EASE_OUT, 200, 0, 0, 0, 400, 350, 0, 1,
				welcomeText, () -> launchHomeScreen())).play();

		// Hiding the loginController.
		logInController.hideForWelcomeMessage();
	}

	/**
	 * Notifies the current {@link ConnectionScreen} that a user hash been added
	 * to the library. Now automatically connecting it.
	 */
	public void onUserAdded(String firstName, String lastName, int cardID, String password, String status) {
		ConnectionScreenRemoteTaskLauncher.connectUserAfterAddInLibrary(this,
				ModelRules.computeUserLogging(lastName, cardID), password);
		// Hiding the loginController.
		signUpController.hideForWelcomeMessage();
	}

	private void launchHomeScreen() {
		HomeScreen homeModule = ModuleLoader.getInstance().load(HomeScreen.class);
		homeModule.initDynamicContent(proxyModel);
		ClientMLVSchool.setNewScreen(300, this, homeModule);
	}

	@FXML
	void signUpButtonClicked() {
		Runnable buttonLogInAction = () -> signUpController.show();
		Runnable buttonSignUpAction = () -> {
		};
		boolean appear = false, fromRight = false;
		buttonsTransition(buttonLogInAction, buttonSignUpAction, appear, fromRight);
	}

	@FXML
	void logInButtonClicked() {
		Runnable buttonLogInAction = () -> logInController.show();
		Runnable buttonSignUpAction = () -> {
		};
		boolean appear = false, fromRight = false;
		buttonsTransition(buttonLogInAction, buttonSignUpAction, appear, fromRight);
	}

	@Override
	public void hide() {
		// Waiting for the Library connection.
		stateMessagesText.setText("Connecting to the Library service");
		stateMessagesText.setLayoutX((paneRoot.getPrefWidth() - stateMessagesText.maxWidth(-1)) / 2f);
		stateMessagesText.setLayoutY((paneRoot.getPrefHeight() - stateMessagesText.maxHeight(-1)) / 2f + 130);
		progressIndicator.setPrefSize(110, 110);
		progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		progressIndicator.setTranslateX((Constants.SCENE_WIDTH - progressIndicator.getPrefWidth()) / 2f);
		progressIndicator.setTranslateY((Constants.SCENE_HEIGHT - progressIndicator.getPrefHeight()) / 2f);
		buttonSignUp.setOpacity(0);
		buttonLogIn.setOpacity(0);
		buttonSignUp.setDisable(true);
		buttonLogIn.setDisable(true);
		titleGroup.setVisible(false);
	}

	@Override
	public void show() {

	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void startHasMainScreen() {
	}

	public void comeBackToState1() {
		double fromX = -200, fromY = 0;
		double toX = 0, toY = 0;
		double transDuration = 400;
		double fadeDuration = 210;
		double fromOpacity = 0, toOpacity = 1;
		Interpolator interpolation = Interpolator.EASE_OUT;
		buttonLogIn.setVisible(true);
		buttonSignUp.setVisible(true);
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonLogIn, () -> buttonLogIn.setDisable(false)).play();
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonSignUp, () -> buttonSignUp.setDisable(false)).play();
	}

	private void buttonsTransition(Runnable buttonLogInAction, Runnable buttonSignUpAction, boolean appear,
			boolean fromRight) {
		double fromX = 200 * (fromRight ? 1 : 0), fromY = 0;
		double toX = -200 * (fromRight ? 0 : 1), toY = 0;
		double transDuration = 400;
		double fadeDuration = 210;
		double fromOpacity = 1 * (appear ? 0 : 1), toOpacity = 1 * (appear ? 1 : 0);
		Interpolator interpolation = Interpolator.EASE_OUT;
		buttonLogIn.setDisable(!appear);
		buttonSignUp.setDisable(!appear);
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonLogIn, buttonLogInAction).play();
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonSignUp, buttonSignUpAction).play();
	}

	private void startTitleGroupAnimation() {
		double fromX = 0, fromY = -100;
		double toX = 0, toY = 0;
		double transDuration = 400;
		double fadeDuration = 700;
		double fromOpacity = 0, toOpacity = 1;
		Interpolator interpolation = Interpolator.EASE_OUT;
		titleGroup.setOpacity(fromOpacity);
		titleGroup.setVisible(true);
		Animations.delay(0, transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration,
				fadeDuration, fromOpacity, toOpacity, titleGroup, () -> {
				})).play();
	}

	private <T extends Module> T loadModule(Class<T> moduleClass) {
		// Loading the module.
		T module = ModuleLoader.getInstance().load(moduleClass);
		// Hiding this module.
		module.hide();
		// Adding the module to the current screen.
		paneRoot.getChildren().add(module.getView());
		return module;
	}

	private void loadFontsWorkaround() {
		// FontManager.
		FontManager fontManager = FontManager.getInstance();
		// Titles.
		title.setFont(fontManager.getFont(SF_DISPLAY_REGULAR, 110));
		subTitle.setFont(fontManager.getFont(SF_DISPLAY_LIGHT, 30));
		// Buttons.
		Font buttonFont = fontManager.getFont(SF_DISPLAY_REGULAR, 32);
		buttonLogIn.setFont(buttonFont);
		buttonSignUp.setFont(buttonFont);
		stateMessagesText.setFont(fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 24));
		footer.setFont(fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 18));
	}
}