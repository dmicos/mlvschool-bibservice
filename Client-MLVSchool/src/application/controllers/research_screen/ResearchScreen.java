package application.controllers.research_screen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.SearchModule;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResearchScreen implements Initializable, Screen {

	@FXML
	private VBox categoriesPane;

	@FXML
	private Pane paneRoot;

	@FXML
	private Text researchTitle;

	@FXML
	private Label keywordsText;

	@FXML
	private VBox booksEntryPane;

	private ProxyModel proxyModel;

	private BurgerMenuModule burgerMenuModule;

	private SearchModule searchModule;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO load fonts.
	}

	@Override
	public void startHasMainScreen() {
		// TODO Auto-generated method stub
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}

	public void initContent(ProxyModel proxyModel, String keywords, List<BookAsynchrone> books,
			BurgerMenuModule burgerMenuModule, SearchModule searchModule) {
		this.proxyModel = proxyModel;
		this.burgerMenuModule = burgerMenuModule;
		this.searchModule = searchModule;
		paneRoot.getChildren().add(burgerMenuModule.getView());
		paneRoot.getChildren().add(searchModule.getView());
		// Loading keyword text.
		keywordsText.setText(keywords);
		// Creation of BookEntryModules for books.
		loadBookEntries(books);
		// Loading categories button.

	}

	private void loadBookEntries(List<BookAsynchrone> books) {
		int i = 0;
		for (BookAsynchrone b : books) {
			// TODO delete this limit.
			if (i++ > 10) {
				break;
			}
			BookEntryModule module = ModuleLoader.getInstance().load(BookEntryModule.class);
			module.setBook(b);
			booksEntryPane.getChildren().add(module.getView());
		}
	}

	@Override
	public SearchModule getSearchModule() {
		return searchModule;
	}

	@Override
	public BurgerMenuModule getBurgerMenuModule() {
		return burgerMenuModule;
	}
}
