package application.controllers.home_screen;

import static application.utils.Animations.noAction;
import static application.utils.Animations.transitionOpacityAnimation;
import static javafx.animation.Interpolator.EASE_BOTH;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.model.BookAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.ImageProcessors;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BookSpinerModule implements Initializable, Module {

	private static final int Y = 80;
	private static final int X = 82;
	@FXML
	private VBox paneRoot;
	@FXML
	private ImageView book1;
	@FXML
	private ImageView book2;
	@FXML
	private ImageView book3;
	@FXML
	private ImageView book4;
	@FXML
	private ImageView book5;
	@FXML
	private HBox bookHBox;
	@FXML
	private Text title;
	private BookSpinerModule nextSpiner;

	@FXML
	void book1Clicked() {
		System.out.println("Book 1");
	}

	@FXML
	void book2Clicked() {
		System.out.println("Book 2");
	}

	@FXML
	void book3Clicked() {
		System.out.println("Book 3");
	}

	@FXML
	void book4Clicked() {
		System.out.println("Book 4");
	}

	@FXML
	void book5Clicked() {
		System.out.println("Book 5");
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 36));
	}

	/**
	 * Initializes spiner's books.
	 */
	void initContent(List<BookAsynchrone> books, String title) throws IOException {
		Objects.requireNonNull(books);
		this.title.setText(Objects.requireNonNull(title));
		if (books.size() > 5) {
			throw new IllegalArgumentException("Book list have to be less or equal to 5." + books.size());
		}
		for (BookAsynchrone b : books) {
			book1.setImage(ImageProcessors.decodeBase64(b.getImage()));
		}
		layoutRoot();
	}

	private void layoutRoot() {
		paneRoot.setLayoutX(X);
		paneRoot.setLayoutY(Y);
	}

	@Override
	public void hide() {
		paneRoot.setVisible(false);
	}

	@Override
	public void show() {
		paneRoot.setVisible(true);
		paneRoot.setOpacity(0);
		double x = 0;
		Transition showX = transitionOpacityAnimation(EASE_BOTH, x, 20, x, 0, 1000, 800, 0, 1, paneRoot, noAction);
		Transition hideX = transitionOpacityAnimation(EASE_BOTH, x, 0, x, -400, 640, 120, 1, 0, paneRoot, noAction);
		PauseTransition pause = new PauseTransition(new Duration(4000));
		SequentialTransition sequence = new SequentialTransition(showX, pause, hideX);
		pause.setOnFinished(e -> {
			if (nextSpiner != null) {
				nextSpiner.show();
			}
		});
		sequence.setOnFinished(e -> paneRoot.setVisible(false));
		sequence.play();
	}

	String getTitle() {
		return title.getText();
	}

	/**
	 * 
	 * @param nextSpiner
	 *            can be null if no next.
	 */
	void setNextSpiner(BookSpinerModule nextSpiner) {
		this.nextSpiner = nextSpiner;
	}
}
