package application.controllers.home_screen;

import static application.utils.Constants.START_ICON;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.model.BookAsynchrone;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.ImageProcessors;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BookViewerModule implements Initializable, Module {

	private static final Image STAR_FILL_IMAGE = new Image(ClientMLVSchool.class.getResource(START_ICON).toString());

	@FXML
	private Label authorsLabel;

	@FXML
	private Pane paneRoot;

	@FXML
	private ImageView start1;

	@FXML
	private Label titleLabel;

	@FXML
	private HBox paneRoot1;

	@FXML
	private VBox paneForm;

	@FXML
	private ImageView imageView;

	@FXML
	private Button consultButton;

	@FXML
	private Label dateLabel;

	@FXML
	private Button cancelButton;

	@FXML
	private ImageView start3;

	@FXML
	private ImageView start2;

	@FXML
	private ImageView start5;

	@FXML
	private ImageView start4;

	private boolean isShown;

	private ImageView[] stars;

	@FXML
	void paneRootClicked() {
		hide();
	}

	@FXML
	void paneFormClicked() {
		System.out.println("Form : consulted clic");
	}

	@FXML
	void consultCliked() {
		System.out.println("Viewer : consulted clic");
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void hide() {
		if (!isShown) {
			return;
		}
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO font.
		stars = new ImageView[] { start1, start2, start3, start4, start5 };
		// Not visible at first.
		paneRoot.setMouseTransparent(true);
		paneRoot.setTranslateY(Constants.SCENE_HEIGHT);
		isShown = false;
	}

	public void setBook(BookAsynchrone book) {
		Objects.requireNonNull(book);
		titleLabel.setText(book.getTitle());
		dateLabel.setText(book.getDate());
		authorsLabel.setText("De " + book.getAuthors().stream().collect(Collectors.joining(", ")));
		int rate = book.getRate();
		for (int i = 0; i < rate && i < 5; i++) {
			stars[i].setImage(STAR_FILL_IMAGE);
		}
		Thread t = new Thread(() -> {
			Image image;
			try {
				image = ImageProcessors.decodeBase64(book.getImage());
			} catch (Exception e) {
				System.err.println("Image not loaded for : " + book.getTitle());
				return;
			}
			Platform.runLater(() -> {
				imageView.setImage(image);
			});
		});

		t.setDaemon(true);
		t.start();
	}
}
