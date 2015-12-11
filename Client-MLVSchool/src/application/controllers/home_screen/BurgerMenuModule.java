package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BurgerMenuModule implements Initializable, Module {

	@FXML
	private VBox paneRoot;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadFontsWorkaround();
	}

	@FXML
	void homeClicked() {

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
}
