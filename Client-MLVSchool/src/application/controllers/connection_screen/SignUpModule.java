package application.controllers.connection_screen;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.controllers.BindingsLimits;
import application.controllers.Module;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.Transformations;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Sign up module of the connection screen.
 * 
 * @author Jefferson
 *
 */
public class SignUpModule implements Initializable, Module {

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
	@FXML
	private ComboBox<String> statusList;

	// Enclosing controller.
	private ConnectionScreen connectionScreenController;

	// Used for transform calculations.
	private final Point2D tmp = new Point2D(0, 0);

	boolean isSigningUp;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textlogin.textProperty().bind(
				BindingsLimits.toLowerCase(tFLastname.textProperty()).concat(".").concat(tFCardID.textProperty()));
		// Limiting values.
		BindingsLimits.setAlphabeticConstraint(tFFirstname);
		BindingsLimits.addTextLimiter(tFFirstname, Constants.NAME_LENGTH);
		BindingsLimits.setAlphabeticConstraint(tFLastname);
		BindingsLimits.addTextLimiter(tFLastname, Constants.NAME_LENGTH);
		BindingsLimits.addTextLimiter(tFPassword, Constants.PASSWORD_LENGTH);
		BindingsLimits.setNumericConstraint(tFCardID);
		BindingsLimits.addTextLimiter(tFCardID, Constants.CARDID_LENGTH);

		// Setting fonts.
		loadFontWorkAround();
	}

	/**
	 * Called when the sign in operation from this module has failed.
	 */
	void onSignUpFailed() {
		isSigningUp = false;
		buttonSignUp.setDisable(false);
		// Notifying visually with red prompts.
		Consumer<TextField> wrongNotification = tf -> {
			// The method playWrongRectangle needs scene coordinates.
			Point2D pp = Transformations.toSceneCoordinates(tf, tmp);
			Animations.playWrongRectangle(pp.getX(), pp.getY(), tf.getWidth(), tf.getHeight(), paneRoot);
		};
		wrongNotification.accept(tFFirstname);
		wrongNotification.accept(tFLastname);
		wrongNotification.accept(tFCardID);
		wrongNotification.accept(tFPassword);
	}

	@FXML
	public void signUpClicked() {
		if (isSigningUp) {
			return;
		}
		// Indicate a red rectangle marker on the Region r.
		Consumer<Region> wrongMarker = r -> {
			// The method playWrongRectangle needs scene coordinates.
			Point2D pp = Transformations.toSceneCoordinates(r, tmp);
			Animations.playWrongRectangle(pp.getX(), pp.getY(), r.getPrefWidth(), r.getPrefHeight(), paneRoot);
		};

		// Verifying existence of the first name.
		for (TextField tf : new TextField[] { tFFirstname, tFLastname, tFCardID, tFPassword }) {
			if (tf.getText().isEmpty()) {
				wrongMarker.accept(tf);
				return;
			}
		}

		if (tFCardID.getText().length() != 5) {
			wrongMarker.accept(tFCardID);
			return;
		}

		isSigningUp = true;
		buttonSignUp.setDisable(true);

		// Everything is OK. Trying to add user to the database.
		// We can parseInt thanks to the controller inputs restrictions.
		ConnectionScreenRemoteTaskLauncher.addUser(connectionScreenController, this, tFFirstname.getText(),
				tFLastname.getText(), Integer.parseInt(tFCardID.getText()), tFPassword.getText(),
				statusList.getValue());
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

	void setStatusList(List<String> statusList) {
		Objects.requireNonNull(statusList);
		this.statusList.getItems().addAll(statusList);
		this.statusList.setValue(statusList.get(0));
	}

	public void setConnectionScreenController(ConnectionScreen connectionScreenController) {
		this.connectionScreenController = Objects.requireNonNull(connectionScreenController);
	}

	private void loadFontWorkAround() {
		FontManager fontManager = FontManager.getInstance();
		Font font = fontManager.getFont(Constants.SF_TEXT_REGULAR, 24);
		Font font2 = fontManager.getFont(Constants.SF_DISPLAY_REGULAR, 32);
		Font font3 = fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 24);
		Font font4 = fontManager.getFont(Constants.SF_TEXT_LIGHT, 24);
		tFFirstname.setFont(font);
		tFLastname.setFont(font);
		tFCardID.setFont(font);
		tFPassword.setFont(font);
		textlogin.setFont(font3);
		buttonSignUp.setFont(font2);
		buttonCancel.setFont(font2);
		textCancel.setFont(font4);
	}
}
