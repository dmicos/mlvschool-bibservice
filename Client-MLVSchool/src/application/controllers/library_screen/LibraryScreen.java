package application.controllers.library_screen;

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

	@Override
	public Pane getView() {
		return paneRoot;
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
		// TODO add 2 spinner.
		UserAsynchrone user = proxyModel.getConnectedUser();
		List<BookAsynchrone> books = user.getBooks();
		List<BookAsynchrone> pendingBooks = user.getPendingBooks();

		BookSpinerModule spiner1 = BookSpinerModule.initStaticSpinner(books);
		BookSpinerModule spiner2 = BookSpinerModule.initStaticSpinner(pendingBooks);
		paneRoot.getChildren().addAll(spiner1.getView(), spiner2.getView());
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
