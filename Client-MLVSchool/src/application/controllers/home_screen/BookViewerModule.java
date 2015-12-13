package application.controllers.home_screen;

import static application.utils.Constants.START_ICON;
import static application.utils.NotificationsManager.NotificationType.INFO;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import application.ClientMLVSchool;
import application.controllers.BindingsLimits;
import application.controllers.Module;
import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskLauncher;
import application.controllers.RemoteTaskObserver;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Animations;
import application.utils.Constants;
import application.utils.CoordinateTransformations;
import application.utils.ImageProcessors;
import application.utils.NotificationsManager;
import fr.upem.rmirest.bilmancamp.interfaces.BookComment;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BookViewerModule implements Initializable, Module, RemoteTaskObserver {

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
	private Button actionButton;

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

	@FXML
	private VBox commentPane;

	@FXML
	private TextField commentaryField;

	@FXML
	private TextField rateField;

	private boolean isShown;

	private ImageView[] stars;

	private enum State {
		COMMENTARY_POSTING, HIDLE;
	}

	private State state = State.HIDLE;

	private BookAsynchrone book;

	private final Point2D tmp = new Point2D(0, 0);

	private ProxyModel proxyModel;

	private enum BookState {
		TO_BORROW, TO_GIVE_BACK, TO_CANCEL;
	}

	private BookState bookState;

	@FXML
	void paneRootClicked() {
		hide();
	}

	@FXML
	void paneFormClicked() {
		// hide arrange this "bug".
	}

	@FXML
	void actionButtonClicked() {
		System.out.println("Button action clicked. " + bookState);
		switch (bookState) {
		case TO_BORROW:
			RemoteTaskLauncher.borrowBook(proxyModel, proxyModel.getConnectedUser(), book, this);
			break;
		case TO_GIVE_BACK:
			RemoteTaskLauncher.giveBack(proxyModel, proxyModel.getConnectedUser(), book, this);
			break;
		case TO_CANCEL:
			RemoteTaskLauncher.cancel(proxyModel, proxyModel.getConnectedUser(), book, this);
			break;
		}
	}

	@FXML
	void consultCliked() {
		System.out.println("Viewer : consulted clic");
	}

	@FXML
	void cancelClicked() {
		hide();
	}

	@Override
	public void onBookBorrowed(Boolean borrowed, BookAsynchrone book, UserAsynchrone user) {
		RemoteTaskObserver.super.onBookBorrowed(borrowed, book, user);
		configureActionButton(proxyModel, book);
		// TODO Reload comments too.
	}

	@Override
	public void onBookGivenBack(Boolean giveBack, BookAsynchrone book, UserAsynchrone user) {
		RemoteTaskObserver.super.onBookGivenBack(giveBack, book, user);
		configureActionButton(proxyModel, book);
		// TODO Reload comments too.
	}

	@Override
	public void onBookCancel(Boolean cancel, BookAsynchrone book, UserAsynchrone user) {
		RemoteTaskObserver.super.onBookGivenBack(cancel, book, user);
		configureActionButton(proxyModel, book);
		// TODO Reload comments too.
	}

	private void commentaryRequested() {
		if (state == State.COMMENTARY_POSTING) {
			return;
		}
		// Notifying visually with red prompts.
		Consumer<TextField> wrongNotification = tf -> {
			// The method playWrongRectangle needs scene coordinates.
			Point2D pp = CoordinateTransformations.toSceneCoordinates(tf, tmp);
			Animations.playWrongRectangle(pp.getX(), pp.getY(), tf.getWidth(), tf.getHeight(), paneRoot);
		};
		String content = commentaryField.getText();
		if (content.isEmpty() || content.matches("\\s+")) {
			wrongNotification.accept(commentaryField);
			return;
		}
		if (rateField.getText().isEmpty()) {
			wrongNotification.accept(rateField);
			return;
		}
		int rate = Integer.parseInt(rateField.getText());
		state = State.COMMENTARY_POSTING;
		// RemoteTaskLauncher.searchBooks(keywords, proxyModel, this);
		// TODO POST A COMMENTRY WITH BEING THE OBSERVER. WHEN THE COMMENT HAS
		// BEEN DONE, RELOAD THE VIEWER, (ONLY THE COMMENTARY SECTION, NOT THE
		// STRING->IMAGE LOADING) PUT THE STATE IN HIDLE, EMPTY THE TEXT AREA,
		// PUSH A NOTIFICATION.
		// TODO delete this call, it's fake. AND PASSE THE RATING TO !!
		RemoteTaskLauncher.addComment(proxyModel, book, content, rate, this);
	}

	@Override
	public void onCommentaryOnBookPosted(BookAsynchrone book) {
		if (state == State.HIDLE) {
			return;
		}
		state = State.HIDLE;
		commentaryField.setText("");
		NotificationsManager.notify("Thank you", "Your commentary is now in the Library", INFO);
		commentPane.getChildren().clear();
		loadComments(book);
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
		// Setting the listening on "ENTER" for commentary & rate fields.
		commentaryField.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				commentaryRequested();
			}
		});
		rateField.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				commentaryRequested();
			}
		});
		// Rate field is limiting in [0-5];
		BindingsLimits.setNumericeRateConstraint(rateField);
	}

	public void setData(ProxyModel proxyModel, BookAsynchrone book) {
		this.proxyModel = Objects.requireNonNull(proxyModel);
		this.book = Objects.requireNonNull(book);
		titleLabel.setText(book.getTitle());
		dateLabel.setText(book.getDate());
		authorsLabel.setText("De " + book.getAuthors().stream().collect(Collectors.joining(", ")));
		int rate = book.getRate();

		// TODO no workaround have to be left. Commentary.
		loadComments(book);

		for (int i = 0; i < rate && i < 5; i++) {
			stars[i].setImage(STAR_FILL_IMAGE);
		}

		// The text & colors on the button.
		configureActionButton(proxyModel, book);

		// Loading the image in a separate worker.
		loadImageInWorker(book);
	}

	private void configureActionButton(ProxyModel proxyModel, BookAsynchrone book) {
		bookState = BookState.TO_BORROW;
		actionButton.setText("Borrow");
		UserAsynchrone connectedUser = proxyModel.getConnectedUser();
		for (BookAsynchrone uBook : connectedUser.getBooks()) {
			if (uBook.getId() == book.getId()) {
				// The book is already took.
				bookState = BookState.TO_GIVE_BACK;
				actionButton.setText("Give back");
				return;
			}
		}
		// Checking if the book is in the pending list.
		for (BookAsynchrone uBook : connectedUser.getPendingBooks()) {
			if (uBook.getId() == book.getId()) {
				// The book is already in queue, we can cancel.
				bookState = BookState.TO_CANCEL;
				actionButton.setText("Cancel");
				return;
			}
		}
		// Here we are sure the user can request a BORROW.
		// Keeping the button the same as default.
	}

	private void loadImageInWorker(BookAsynchrone book) {
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



	private void loadComments(BookAsynchrone book) {
		List<BookComment> comments = book.getComments();
		for (BookComment comment : comments) {
			BookCommentModule commentModule = ModuleLoader.getInstance().load(BookCommentModule.class);
			commentModule.setComment(comment.getAuthors(), comment.getContent());
			commentPane.getChildren().add(commentModule.getView());
		}
	}
	
//	private void loadComments(BookAsynchrone book) {
//		String commentAuthor = book.getCommentAuthor();
//		List<String> commentsContent = book.getCommentText();
//		for (String comment : commentsContent) {
//			BookCommentModule commentModule = ModuleLoader.getInstance().load(BookCommentModule.class);
//			commentModule.setComment(commentAuthor, comment);
//			commentPane.getChildren().add(commentModule.getView());
//		}
//	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
