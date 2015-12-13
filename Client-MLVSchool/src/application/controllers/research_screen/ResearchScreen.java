package application.controllers.research_screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResearchScreen implements Initializable, Screen {

	@FXML
	private VBox categoryButtonPane;

	@FXML
	private Pane paneRoot;

	@FXML
	private Text researchTitle;

	@FXML
	private Label keywordsText;
	
	@FXML
	private Text researchSubTitle;

	@FXML
	private VBox booksEntryPane;

	private ProxyModel proxyModel;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		researchTitle.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 52));
		keywordsText.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 24));
		researchSubTitle.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 36));
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}

	@Override
	public void initializeWithDynamicContent(ProxyModel proxyModel) {
		this.proxyModel = proxyModel;
	}

	public void loadBooks(String keywords, List<BookAsynchrone> books) {
		// Loading keyword text.
		keywordsText.setText(keywords);
		// Creation of BookEntryModules for books.
		loadBookEntries(books);
		// Loading categories button.
		List<String> categories = proxyModel.getLibrary().getCategories();
		for (String category : categories) {
			CategoryButtonModule buttonModule = ModuleLoader.getInstance().load(CategoryButtonModule.class);
			buttonModule.initializeContent(this, category);
			categoryButtonPane.getChildren().add(buttonModule.getView());
		}
	}

	private void loadBookEntries(List<BookAsynchrone> books) {
		// Loading images in worker thread !!
		List<Node> viewsTmp = new ArrayList<>();
		Thread t = new Thread(() -> {
			int i = 0;
			for (BookAsynchrone b : books) {
				BookEntryModule module = ModuleLoader.getInstance().load(BookEntryModule.class);
				module.setBook(b);
				viewsTmp.add(module.getView());
				i++;
				if (i > 30) {
					i = 0;
					sendToJAVAFX(viewsTmp);
					viewsTmp.clear();
				}
			}
			// Last sent to send the remaining.
			sendToJAVAFX(viewsTmp);
		});
		t.setDaemon(true);
		t.start();
	}

	private void sendToJAVAFX(List<Node> viewsTmp) {
		List<Node> producer = new ArrayList<>(viewsTmp);
		Platform.runLater(() -> {
			for (Node node : producer) {
				booksEntryPane.getChildren().add(node);
			}
		});
	}
}
