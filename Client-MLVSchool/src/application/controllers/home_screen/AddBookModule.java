package application.controllers.home_screen;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.ClientMLVSchool;
import application.controllers.BindingsLimits;
import application.controllers.Module;
import application.controllers.RemoteTaskLauncher;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.CoordinateTransformations;
import application.utils.FontManager;
import application.utils.ImageProcessors;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class AddBookModule implements Initializable, Module {

	private static final int IMAGE_MAX_SIZE = 30000;
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
	private ChoiceBox<String> categories;
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

	private HomeScreen enclosingScreen;

	// Used for transform calculations.
	private final Point2D tmp = new Point2D(0, 0);

	/* State variable */
	private boolean isShown;

	private ImageView viewerTmp;
	private String imageBase64;

	@FXML
	public void paneRootClicked() {
		hide();
	}

	@FXML
	public void paneFormClicked() {
		// hide(); Used to prevent the root to be clicked & hidded.
	}

	@FXML
	public void cancelClicked() {
		hide();
	}

	public void onBookAdded(String title) {
		NotificationsManager.notify("Succes", "Book " + title + " successfully added.", NotificationType.DATABASE);
		clearForm();
		enclosingScreen.reloadSpinnerBooks();
	}

	private void clearForm() {
		fieldTitle.setText("");
		fieldTitle.setText("");
		fieldAbstract.setText("");
		fieldPrice.setText("");
		// Not required.
		fieldAuthor1.setText("");
		fieldAuthor2.setText("");
		fieldAuthor3.setText("");
		fieldAuthor4.setText("");
		imageBase64 = "";
		viewerTmp.setImage(null);
		addImageButton.setVisible(true);
	}

	@FXML
	public void addClicked() {

		// Parsing prompts.
		// Processing fields and stopping at null. (process field indicated the
		// error.) Required fields.
		String title = processField(fieldTitle);
		String summary = processField(fieldAbstract);
		String price = processField(fieldPrice);

		// Not required.
		List<String> tags = Arrays.asList(fieldTags.getText().split("\\s+"));
		String author1 = fieldAuthor1.getText();
		String author2 = fieldAuthor2.getText();
		String author3 = fieldAuthor3.getText();
		String author4 = fieldAuthor4.getText();
		String category = categories.getValue();

		System.out.println("Tags : " + tags);
		if (title == null || summary == null || price == null) {
			return;
		}

		if (viewerTmp.getImage() == null) {
			NotificationsManager.notify("Error", "Please select an image", NotificationType.DATABASE);
			return;
		}

		// Everything is done.
		List<String> authors = formatAuthors(author1, author2, author3, author4);
		String mainImage = imageBase64;
		List<String> secondaryImages = new ArrayList<>();
		List<String> cats = new ArrayList<>();
		cats.add(category);
		double priceValue = 0;
		try {
			priceValue = Double.parseDouble(price);
		} catch (NumberFormatException e) {
			System.err.println("Price error : " + price);
		}

		// Sending.
		hide();
		NotificationsManager.notify("Information", "Sending : " + title + " to the library", NotificationType.DATABASE);
		RemoteTaskLauncher.addBook(enclosingScreen, this, title, authors, summary, mainImage, secondaryImages, cats,
				priceValue, tags);
	}

	public void onBookAddedError() {
		NotificationsManager.notify("Information",
				"An error has occured during the registration\nPlease verify the informations.",
				NotificationType.DATABASE);
		show();
	}

	private List<String> formatAuthors(String author1, String author2, String author3, String author4) {
		List<String> authors = new ArrayList<>();
		authors.add(author1);
		// Not very beautiful, but neh.
		if (author2 != null) {
			authors.add(author2);
		}
		if (author3 != null) {
			authors.add(author4);
		}
		if (author4 != null) {
			authors.add(author4);
		}
		return authors;
	}

	@FXML
	public void searchImageClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("70 x 145");
		File file = null;
		if (null == (file = fileChooser.showOpenDialog(ClientMLVSchool.getINSTANCE().getStage()))) {
			return;
		}

		// Loading the image.
		try {
			imageBase64 = ImageProcessors.encodeBase64(file.toString());
			// Checking the size.
			if (imageBase64.length() > IMAGE_MAX_SIZE) {
				NotificationsManager.notify("Error", "Image to big ! \n" + file, NotificationType.DATABASE);
				return;
			}
			Image image = ImageProcessors.create(file);
			viewerTmp.setImage(image);
			viewerTmp.setFitWidth(120);
			viewerTmp.setFitHeight(145);
			viewerTmp.setPreserveRatio(true);
			Point2D p = CoordinateTransformations.toSceneCoordinates(addImageButton, new Point2D(0, 0));
			viewerTmp.setLayoutX((addImageButton.getWidth() - viewerTmp.getFitWidth()) / 2f + p.getX());
			viewerTmp.setLayoutY((addImageButton.getHeight() - viewerTmp.getFitHeight()) / 2f + p.getY());
			paneRoot.getChildren().add(viewerTmp);
		} catch (Exception e) {
			System.err.println("Path : " + file + " Error : " + e.getMessage());
			NotificationsManager.notify("Error", "Cannot load the image at : \n" + file, NotificationType.INFO);
			return;
		}
		addImageButton.setVisible(false);
		NotificationsManager.notify("Succes", "Image successfully loaded : \n" + file, NotificationType.INFO);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewerTmp = new ImageView();
		loadWorkAroundFont();
		paneRoot.setMouseTransparent(true);
		paneRoot.setTranslateY(Constants.SCENE_HEIGHT);
		isShown = false;
		textButtonExistingBook.setDisable(true);
		limitFieldsBindings();
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

	void setCategories(List<String> categories) {
		Objects.requireNonNull(categories);
		ObservableList<String> items = FXCollections.observableArrayList(categories);
		this.categories.setItems(items);
		if (items.size() > 0) {
			this.categories.setValue(items.get(0));
		}
	}

	private void limitFieldsBindings() {
		BindingsLimits.addTextLimiter(fieldTitle, 150);
		BindingsLimits.addTextLimiter(fieldAuthor1, 30);
		BindingsLimits.addTextLimiter(fieldAuthor2, 30);
		BindingsLimits.addTextLimiter(fieldAuthor3, 30);
		BindingsLimits.addTextLimiter(fieldAuthor4, 30);
		BindingsLimits.addTextLimiter(fieldAbstract, 410);
		BindingsLimits.setNumericFloatConstraint(fieldPrice);
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

	private String processField(TextInputControl fieldTitle) {
		Consumer<Region> wrongMarker = r -> {
			// The method playWrongRectangle needs scene coordinates.
			Point2D pp = CoordinateTransformations.toSceneCoordinates(r, tmp);
			Animations.playWrongRectangle(pp.getX(), pp.getY(), r.getPrefWidth(), r.getPrefHeight(), paneRoot);
		};

		String result = fieldTitle.getText();
		if (result.isEmpty()) {
			wrongMarker.accept(fieldTitle);
			return null;
		}
		return result;
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	void setScreen(HomeScreen screen) {
		enclosingScreen = Objects.requireNonNull(screen);
	}
}
