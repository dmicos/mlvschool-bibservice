package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BurgerMenuModule implements Initializable, Module {
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
	private Button profilButton;
	@FXML
	private Button libraryButton;
	@FXML
	private Button addBookButton;
	@FXML
	private Button logOutButton;
	@FXML
	private Text logOutText;

	/* Whether the menu is hidden or not */
	private boolean isHidden = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paneRoot.setTranslateX(-BURGER_WIDTH);
		loadFontsWorkaround();
	}

	@FXML
	public void homeClicked() {
	}

	@FXML
	public void libraryClicked() {
	}

	@FXML
	public void addBookClicked() {

	}

	@FXML
	public void logOutClicked() {

	}

	@FXML
	public void burgerClicked() {
		if (isHidden) {
			Animations.transition(-BURGER_WIDTH, 0, 500, Interpolator.LINEAR, paneRoot).play();
			isHidden = false;
			return;
		}
		Animations.transition(0, -BURGER_WIDTH, 500, Interpolator.LINEAR, paneRoot).play();
		isHidden = true;
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
		profilButton.setFont(font1);
		libraryButton.setFont(font1);
		addBookButton.setFont(font1);
		logOutText.setFont(font2);
		firstName.setFont(font3);
		lastName.setFont(font3);
	}

	public void setUserInfo(String firstName, String lastName) {
		this.firstName.setText(firstName);
		this.lastName.setText(lastName);
	}
}
