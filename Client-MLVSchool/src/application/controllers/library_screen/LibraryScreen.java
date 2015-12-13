package application.controllers.library_screen;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.controllers.RemoteTaskObserver;
import application.controllers.Screen;
import application.controllers.home_screen.BookSpinerModule;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LibraryScreen implements Screen, RemoteTaskObserver, Initializable {

	@FXML
	private Pane paneRoot;

	@FXML
	private Text screenTitle;

	@FXML
	private Text myPendingsText;

	@FXML
	private Text myBooksText;

	private ProxyModel proxyModel;

	private BookSpinerModule spiner1;

	private BookSpinerModule spiner2;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void onLibraryRefreshed() {
		try {
			spiner1.initContent(proxyModel.getConnectedUser().getBooks(), "");
			spiner2.initContent(proxyModel.getConnectedUser().getPendingBooks(), "");
		} catch (IOException e) {
		}
	}

	@Override
	public void onBookBorrowed(Boolean borrowed, BookAsynchrone book, UserAsynchrone user) {
		try {
			System.out.println("Updating borrowed books.");
			spiner1.initContent(user.getBooks(), "");
			spiner2.initContent(user.getPendingBooks(), "");
		} catch (IOException e) {
		}
	}

	@Override
	public void onBookGivenBack(Boolean giveBack, BookAsynchrone book, UserAsynchrone user) {
		try {
			System.out.println("Updating given back books.");
			spiner1.initContent(user.getBooks(), "");
			spiner2.initContent(user.getPendingBooks(), "");
		} catch (IOException e) {
		}
	}

	@Override
	public void onBookCancel(Boolean cancel, BookAsynchrone book, UserAsynchrone user) {
		// No call on super. It has a design problem with the number of obs will
		// call the same super x time in the chain :S. I have to delegate to
		// only one.
		try {
			System.out.println("Updating pending books.");
			spiner1.initContent(user.getBooks(), "");
			spiner2.initContent(user.getPendingBooks(), "");
		} catch (IOException e) {
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FontManager fontManager = FontManager.getInstance();
		screenTitle.setFont(fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 52));
		Font font = fontManager.getFont(Constants.SF_TEXT_SEMIBOLD, 36);
		myBooksText.setFont(font);
		myPendingsText.setFont(font);
	}

	@Override
	public void initializeWithDynamicContent(ProxyModel proxyModel) {
		this.proxyModel = proxyModel;
		UserAsynchrone user = proxyModel.getConnectedUser();
		List<BookAsynchrone> books = user.getBooks();
		List<BookAsynchrone> pendingBooks = user.getPendingBooks();

		System.out.println("Book size : " + books.size());
		System.out.println("Pending size : " + pendingBooks.size());
		spiner1 = BookSpinerModule.initStaticSpinner(books);
		spiner2 = BookSpinerModule.initStaticSpinner(pendingBooks);
		spiner2.getView().setLayoutY(500);
		paneRoot.getChildren().addAll(spiner1.getView(), spiner2.getView());
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
