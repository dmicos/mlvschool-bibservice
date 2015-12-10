package application.controllers.connection_screen;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.controllers.BindingsLimits;
import application.controllers.Module;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Log In Module of the connection screen.
 * 
 * @author Jefferson
 *
 */
public class LogInModule implements Initializable, Module {

	@FXML
	private Pane paneRoot;
	@FXML
	private Text hintText;
	@FXML
	private TextField tFLogin;
	@FXML
	private TextField tFPassword;
	@FXML
	private Button buttonLogin;
	@FXML
	private Button buttonCancel;

	// Enclosing screen
	private ConnectionScreen connectionScreenController;

	private boolean isLogingIn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadFontWorkAround();
		BindingsLimits.setLoginConstraint(tFLogin);
		BindingsLimits.addTextLimiter(tFLogin, Constants.LOGGING_LENGTH);
		BindingsLimits.addTextLimiter(tFPassword, Constants.PASSWORD_LENGTH);
	}

	/**
	 * Notifies the current {@link LogInModule} that the login operation has not
	 * occurred. Due to wrong informations.
	 */
	public void onLoginError() {
		isLogingIn = false;
		buttonLogin.setDisable(false);
		Animations.playWrongRectangle(tFLogin.getLayoutX(), tFLogin.getLayoutY(), tFLogin.getWidth(),
				tFLogin.getHeight(), paneRoot);
		Animations.playWrongRectangle(tFPassword.getLayoutX(), tFPassword.getLayoutY(), tFPassword.getWidth(),
				tFPassword.getHeight(), paneRoot);
	}

	@FXML
	private void logInClicked() {
		if (isLogingIn) {
			return;
		}
		// Indicate a red rectangle marker on the Region r.
		Consumer<Region> wrongMarker = r -> Animations.playWrongRectangle(r.getLayoutX(), r.getLayoutY(),
				r.getPrefWidth(), r.getPrefHeight(), paneRoot);

		// Verifying existence of the login.
		String login = tFLogin.getText();
		if (login.isEmpty()) {
			wrongMarker.accept(tFLogin);
			return;
		}

		// Verifying existence of the password.
		String password = tFPassword.getText();
		if (password.isEmpty()) {
			wrongMarker.accept(tFPassword);
			return;
		}
		isLogingIn = true;
		buttonLogin.setDisable(true);
		// Everything is OK. Trying to log to the database.
		ConnectionScreenRemoteTaskLauncher.connectUser(connectionScreenController, this, login, password);
	}

	@FXML
	public void cancelClicked() {
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 0, 0, 200, 0, 400, 210, 1, 0, paneRoot, () -> {
			hide();
			connectionScreenController.comeBackToState1();
		}).play();
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void hide() {
		paneRoot.setVisible(false);
	}

	@Override
	public void show() {
		paneRoot.setVisible(true);
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 200, 0, 0, 0, 400, 250, 0, 1, paneRoot, () -> {
		}).play();
	}

	void hideForWelcomeMessage() {
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 0, 0, -200, 0, 400, 250, 1, 0, paneRoot,
				() -> paneRoot.setVisible(false)).play();
	}

	/*
	 * This could be in an abstract class with SignUp/LogInControllers but for 2
	 * classes, we're not here to speculate.
	 */
	void setConnectionScreenController(ConnectionScreen connectionScreenController) {
		this.connectionScreenController = Objects.requireNonNull(connectionScreenController);
	}

	private void loadFontWorkAround() {
		FontManager fontManager = FontManager.getInstance();
		Font font1 = fontManager.getFont(Constants.SF_TEXT_REGULAR, 24);
		Font font2 = fontManager.getFont(Constants.SF_DISPLAY_REGULAR, 32);
		Font font3 = fontManager.getFont(Constants.SF_TEXT_LIGHT, 24);
		Font font4 = fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 24);
		tFLogin.setFont(font1);
		tFPassword.setFont(font1);
		buttonLogin.setFont(font2);
		buttonCancel.setFont(font3);
		hintText.setFont(font4);
	}
}
