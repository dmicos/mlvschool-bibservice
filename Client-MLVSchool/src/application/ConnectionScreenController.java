package application;

import static application.utils.Constants.SF_DISPLAY_REGULAR;
import static application.view.animations.Animations.transitionOpacityAnimation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.LogInController;
import application.controllers.ModuleLoader;
import application.controllers.SignUpController;
import application.utils.Constants;
import application.utils.FontManager;
import application.view.animations.Animations;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ConnectionScreenController implements Initializable {

	@FXML
	private Pane paneRoot;
	@FXML
	private Button buttonSignUp;
	@FXML
	private Button buttonLogIn;

	/* Title */
	@FXML
	private Text titleID;
	@FXML
	private VBox titleGroup;

	// SignUp module.
	private Pane signUpModuleView;
	private SignUpController signUpController;
	// LogIn module.
	private Pane logInModuleView;
	private LogInController logInController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// FontManager.
		FontManager fontManager = FontManager.getInstance();
		// Titles.
		titleID.setFont(fontManager.getFont(SF_DISPLAY_REGULAR, 110));
		// Buttons.
		Font buttonFont = fontManager.getFont(SF_DISPLAY_REGULAR, 32);
		buttonLogIn.setFont(buttonFont);
		buttonSignUp.setFont(buttonFont);

		// Loading modules.
		loadSignUpModule();
		loadLogInModule();

		// Animation.
		startTitleGroupAnimation();
	}

	@FXML
	void signUpButtonClicked() {
		Runnable buttonLogInAction = () -> signUpController.showAnimation();
		Runnable buttonSignUpAction = () -> System.out.println("Signing Up !");
		buttonTransitionAnimation(buttonLogInAction, buttonSignUpAction);
	}

	@FXML
	void logInButtonClicked() {
		Runnable buttonLogInAction = () -> logInController.showAnimation();
		Runnable buttonSignUpAction = () -> {
		};
		buttonTransitionAnimation(buttonLogInAction, buttonSignUpAction);
	}

	public void comeBackToState1() {
		double fromX = -200, fromY = 0;
		double toX = 0, toY = 0;
		double transDuration = 400;
		double fadeDuration = 210;
		double fromOpacity = 0, toOpacity = 1;
		Interpolator interpolation = Interpolator.EASE_OUT;
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonLogIn, () -> buttonLogIn.setDisable(false)).play();
		transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration, fadeDuration, fromOpacity,
				toOpacity, buttonSignUp, () -> buttonSignUp.setDisable(false)).play();
	}

	private void buttonTransitionAnimation(Runnable buttonLogInAction, Runnable buttonSignUpAction) {
		double fromX = 0, fromY = 0;
		double toX = -200, toY = 0;
		double transDuration = 400;
		double fadeDuration = 210;
		double fromOpacity = 1, toOpacity = 0;
		Interpolator interpolation = Interpolator.EASE_OUT;
		buttonLogIn.setDisable(true);
		buttonSignUp.setDisable(true);
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
		Animations.delay(800, transitionOpacityAnimation(interpolation, fromX, fromY, toX, toY, transDuration,
				fadeDuration, fromOpacity, toOpacity, titleGroup, () -> {
				})).play();
	}

	private void loadSignUpModule() {
		try {
			FXMLLoader loader = ModuleLoader.loadModule(Constants.CONNECTION_SIGNUP_MODULE);
			signUpModuleView = ModuleLoader.getView(loader);
			signUpController = loader.getController();
			// Configuring the signUpModule initial state.
			signUpController.setConnectionScreenController(this);
			signUpController.hide();

			// Adding modules to the current view.
			paneRoot.getChildren().add(signUpModuleView);
		} catch (IOException e) {
			throw new IllegalStateException(
					"ConnectionScreenController canot load : " + Constants.CONNECTION_SIGNUP_MODULE, e.getCause());
		}
	}

	private void loadLogInModule() {
		String moduleName = Constants.CONNECTION_LOGIN_MODULE;
		try {
			FXMLLoader loader = ModuleLoader.loadModule(moduleName);
			logInModuleView = ModuleLoader.getView(loader);
			logInController = loader.getController();
			// Configuring the signUpModule initial state.
			logInController.setConnectionScreenController(this);
			logInController.hide();

			// Adding modules to the current view.
			paneRoot.getChildren().add(logInModuleView);
		} catch (IOException e) {
			throw new IllegalStateException("ConnectionScreenController canot load : " + moduleName, e.getCause());
		}
	}
}