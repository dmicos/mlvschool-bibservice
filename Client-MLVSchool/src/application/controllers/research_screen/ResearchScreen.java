package application.controllers.research_screen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	private VBox booksEntryPane;

	private ProxyModel proxyModel;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO load fonts.
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
		Thread t = new Thread(() -> {
			for (BookAsynchrone b : books) {
				BookEntryModule module = ModuleLoader.getInstance().load(BookEntryModule.class);
				module.setBook(b);
				Platform.runLater(() -> {
					booksEntryPane.getChildren().add(module.getView());
				});
			}
		});
		t.setDaemon(true);
		t.start();
	}
}
