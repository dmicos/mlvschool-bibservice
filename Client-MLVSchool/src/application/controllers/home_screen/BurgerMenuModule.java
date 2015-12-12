package application.controllers.home_screen;

import static application.utils.Animations.transitionX;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.utils.Animations;
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

	/* Module to add a new Book */
	private AddBookModule addBookModule;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paneRoot.setTranslateX(-BURGER_WIDTH);
		loadFontsWorkaround();
	}

	@FXML
	public void homeClicked() {
		System.out.println("Home button clicked.");
	}

	@FXML
	public void libraryClicked() {
		System.out.println("Library button clicked.");
	}

	@FXML
	public void addBookClicked() {
		addBookModule.show();
		hide();
		System.out.println("Add button clicked.");
	}

	@FXML
	public void logOutClicked() {
		System.out.println("Log out button clicked.");
	}

	@FXML
	public void profilClicked() {
		System.out.println("Profil button clicked.");
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

	void setAddBookModule(AddBookModule addBookModule) {
		this.addBookModule = Objects.requireNonNull(addBookModule);
	}
}
