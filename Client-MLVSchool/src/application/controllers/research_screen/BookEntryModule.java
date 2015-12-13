package application.controllers.research_screen;

import static application.utils.Constants.START_ICON;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.controllers.RemoteTaskLauncher;
import application.model.BookAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.ImageProcessors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BookEntryModule implements Initializable, Module {

	private static final Image STAR_FILL_IMAGE = new Image(ClientMLVSchool.class.getResource(START_ICON).toString());

	@FXML
	private Label dateLabel;

	@FXML
	private Label authorsLabel;

	@FXML
	private HBox paneRoot;

	@FXML
	private ImageView start1;

	@FXML
	private Label titleLabel;

	@FXML
	private HBox titleDatePane;

	@FXML
	private ImageView imageView;

	@FXML
	private ImageView start3;

	@FXML
	private ImageView start2;

	@FXML
	private ImageView start5;

	@FXML
	private ImageView start4;

	@FXML
	private Button consultButton;

	private ImageView stars[];

	private BookAsynchrone book;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@FXML
	public void consultCliked() {
		RemoteTaskLauncher.updateBookToVisualize(book, ClientMLVSchool.getINSTANCE());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Rename this in "stars".
		stars = new ImageView[] { start1, start2, start3, start4, start5 };
		FontManager fontManager = FontManager.getInstance();
		titleLabel.setFont(fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 24));
		dateLabel.setFont(fontManager.getFont(Constants.SF_DISPLAY_LIGHT, 18));
		authorsLabel.setFont(fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 18));
		consultButton.setFont(fontManager.getFont(Constants.SF_DISPLAY_REGULAR, 32));
	}

	/**
	 * Configures the entry on the basis of the given book.
	 */
	public void setBook(BookAsynchrone book) {
		this.book = book;
		titleLabel.setText(book.getTitle());
		dateLabel.setText(book.getDate());
		authorsLabel.setText("De " + book.getAuthors().stream().collect(Collectors.joining(", ")));
		try {
			imageView.setImage(ImageProcessors.decodeBase64(book.getImage()));
		} catch (IOException e) {
			System.err.println("Image not loaded : " + book.getTitle());
		}
		int rate = book.getRate();
		for (int i = 0; i < rate && i < 5; i++) {
			stars[i].setImage(STAR_FILL_IMAGE);
		}
	}

	public void setImage(Image image) {
		imageView.setImage(image);
	}
}
