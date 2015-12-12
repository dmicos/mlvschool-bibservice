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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AddBookModule implements Initializable, Module {

	@FXML
	private Pane paneRoot;
	@FXML
	private Pane paneForm;
	/* Texts */
	@FXML
	private Text inEuroText;
	@FXML
	private Text formTitle;
	@FXML
	private Text categoryText;
	/* Buttons */
	@FXML
	private Button addImageButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button textButtonExistingBook;
	@FXML
	private Button addButton;
	/* Text fields */
	@FXML
	private TextField fieldTitle;
	@FXML
	private TextArea fieldAbstract;
	@FXML
	private TextField fieldPrice;
	@FXML
	private TextField fieldCat1;
	@FXML
	private TextField fieldCat3;
	@FXML
	private TextField fieldCat2;
	@FXML
	private TextField fieldCat4;
	@FXML
	private TextField fieldAuthor3;
	@FXML
	private TextField fieldAuthor4;
	@FXML
	private TextField fieldAuthor1;
	@FXML
	private TextField fieldAuthor2;
	@FXML
	private TextArea fieldTags;

	/* State variable */
	private boolean isShown;

	@FXML
	public void paneRootClicked() {
		hide();
	}

	@FXML
	public void paneFormClicked() {
		// hide(); Used to prevent the root to be clicked & hidded.
	}

	@FXML
	public void addClicked() {
		hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWorkAroundFont();
		paneRoot.setMouseTransparent(true);
		paneRoot.setTranslateY(Constants.SCENE_HEIGHT);
		isShown = false;
	}

	@Override
	public void hide() {
		paneRoot.setMouseTransparent(true);
		paneRoot.setTranslateY(Constants.SCENE_HEIGHT);
		isShown = false;
		Animations.transitionY(0, Constants.SCENE_HEIGHT, 400, Interpolator.EASE_OUT, paneRoot).play();
	}

	@Override
	public void show() {
		if (isShown) {
			return;
		}
		isShown = true;
		paneRoot.setMouseTransparent(false);
		Animations.transitionY(paneRoot.getTranslateY(), 0, 400, Interpolator.EASE_OUT, paneRoot).play();
	}

	private void loadWorkAroundFont() {
		FontManager fontManager = FontManager.getInstance();
		Font font1 = fontManager.getFont(Constants.SF_TEXT_REGULAR, 20);
		Font font2 = fontManager.getFont(Constants.SF_TEXT_LIGHT, 24);
		Font font3 = fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 36);
		Font font4 = fontManager.getFont(Constants.SF_TEXT_REGULAR, 24);
		Font font5 = fontManager.getFont(Constants.SF_DISPLAY_ULTRALIGHT, 24);
		// All fields.
		fieldTitle.setFont(font1);
		fieldAuthor1.setFont(font1);
		fieldAuthor2.setFont(font1);
		fieldAuthor3.setFont(font1);
		fieldAuthor4.setFont(font1);
		fieldAbstract.setFont(font1);
		fieldCat1.setFont(font1);
		fieldCat2.setFont(font1);
		fieldCat3.setFont(font1);
		fieldCat4.setFont(font1);
		fieldTags.setFont(font1);
		fieldTags.setFont(font1);
		// Grey Buttons
		cancelButton.setFont(font2);
		addImageButton.setFont(font2);
		// Pure Text / titles...
		formTitle.setFont(font3);
		categoryText.setFont(font4);
		// Orange indications
		textButtonExistingBook.setFont(font5);
		inEuroText.setFont(font5);
		// Big Orange button
		addButton.setFont(font2);
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}
}
