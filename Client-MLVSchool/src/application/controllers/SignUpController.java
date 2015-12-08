package application.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ConnectionScreenController;
import application.utils.Constants;
import application.utils.FontManager;
import application.view.animations.Animations;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SignUpController implements Initializable {

	@FXML
	private Pane paneRoot;
	@FXML
	private TextField tFFirstname;
	@FXML
	private TextField tFLastname;
	@FXML
	private TextField tFCardID;
	@FXML
	private TextField tFPassword;
	@FXML
	private Text textlogin;
	@FXML
	private Button buttonSignUp;
	@FXML
	private Button buttonCancel;
	@FXML
	private Text textCancel;

	// Enclosing controller.
	private ConnectionScreenController connectionScreenController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textlogin.textProperty().bind(tFFirstname.textProperty().concat(".").concat(tFCardID.textProperty()));
		// Limiting values.
		ControllerDynamicLimiters.setAlphabeticConstraint(tFFirstname);
		ControllerDynamicLimiters.addTextLimiter(tFFirstname, Constants.NAME_LENGTH);
		ControllerDynamicLimiters.setAlphabeticConstraint(tFLastname);
		ControllerDynamicLimiters.addTextLimiter(tFLastname, Constants.NAME_LENGTH);
		ControllerDynamicLimiters.addTextLimiter(tFPassword, Constants.PASSWORD_LENGTH);
		ControllerDynamicLimiters.setNumericConstraint(tFCardID);
		ControllerDynamicLimiters.addTextLimiter(tFCardID, Constants.CARDID_LENGTH);

		// Setting fonts.
		loadLabels();
	}

	@FXML
	public void signUpClicked() {
		System.out.println("Sign up clicked.");
	}

	@FXML
	public void cancelClicked() {
		Animations.transitionOpacityAnimation(Interpolator.EASE_OUT, 0, 0, 200, 0, 400, 210, 1, 0, paneRoot, () -> {
			hide();
			connectionScreenController.comeBackToState1();
		}).play();
	}

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

	private void loadLabels() {
		try {
			FontManager fontManager = FontManager.getInstance();
			Font buttonFont = fontManager.getFont(Constants.SF_TEXT_REGULAR, 24);
			Font buttonFont2 = fontManager.getFont(Constants.SF_DISPLAY_REGULAR, 32);
			tFFirstname.setFont(buttonFont);
			tFLastname.setFont(buttonFont);
			tFCardID.setFont(buttonFont);
			tFPassword.setFont(buttonFont);
			textlogin.setFont(fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 24));
			buttonSignUp.setFont(buttonFont2);
			buttonCancel.setFont(buttonFont2);
			textCancel.setFont(fontManager.getFont(Constants.SF_TEXT_LIGHT, 24));
		} catch (Exception e) {
			System.err.println("ERROR : " + e.getMessage());
			// Can't do something else here.
		}
	}
}
