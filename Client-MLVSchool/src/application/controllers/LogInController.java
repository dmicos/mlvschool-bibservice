package application.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ConnectionScreenController;
import application.utils.Constants;
import application.view.animations.Animations;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class LogInController implements Initializable {

	@FXML
	private Pane paneRoot;
	@FXML
	private TextField tFLogin;
	@FXML
	private TextField tFPassword;
	@FXML
	private Button buttonLogin;
	@FXML
	private Button buttonCancel;
	private ConnectionScreenController connectionScreenController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ControllerDynamicLimiters.addTextLimiter(tFLogin, Constants.LOGGING_LENGTH);
		ControllerDynamicLimiters.setAlphabeticConstraint(tFLogin);
		ControllerDynamicLimiters.addTextLimiter(tFPassword, Constants.PASSWORD_LENGTH);
	}

	@FXML
	public void cancelClicked() {
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 0, 0, 200, 0, 400, 210, 1, 0, paneRoot, () -> {
			hide();
			connectionScreenController.comeBackToState1();
		}).play();
	}

	/*
	 * This could be in an abstract class with SignUp/LogInControllers but for 2
	 * classes, we're not here to speculate.
	 */
	public void setConnectionScreenController(ConnectionScreenController connectionScreenController) {
		this.connectionScreenController = Objects.requireNonNull(connectionScreenController);
	}

	public void hide() {
		paneRoot.setVisible(false);
	}

	public void showAnimation() {
		paneRoot.setVisible(true);
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 200, 0, 0, 0, 400, 250, 0, 1, paneRoot, () -> {
		}).play();
	}
}
