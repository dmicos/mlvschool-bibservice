package application.controllers.home_screen;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.model.LibraryAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HomeScreen implements Initializable, Screen {

	@FXML
	private Pane paneRoot;

	@FXML
	private Text screenTitle;

	@FXML
	private GridPane categoryGrid;

	/* BurgerMenu module on the left of the screen */
	private BurgerMenuModule burgerMenuModule;
	private AddBookModule addBookModule;
	private SearchModule searchModule;

	/* Model fields controlled */
	private ProxyModel proxyModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadModules();
		loadWorkAroundFont();
		// loadDynamicBookSpiner();
	}

	/*
	 * private void loadDynamicBookSpiner() { try { ImageView v = new
	 * ImageView(ImageProcessors.decodeBase64(BookAsynchrone.BOOK));
	 * v.setLayoutX(104); v.setLayoutY(170); paneRoot.getChildren().add(v); //
	 * To add in a Paner in background of // the singer.
	 * paneRoot.getChildren().remove(burgerMenuModule.getView());
	 * paneRoot.getChildren().add(burgerMenuModule.getView()); } catch
	 * (IOException e) { e.printStackTrace(); System.err.println(
	 * "No contractuel available, juste put the delut action !"); return; } }
	 */

	@Override
	public void startHasMainScreen(ProxyModel proxyModel) {
		this.proxyModel = Objects.requireNonNull(proxyModel);
		UserAsynchrone user = Objects.requireNonNull(proxyModel.getConnectedUser());

		// Setting the user informations in the burgerMenu.
		burgerMenuModule.setUserInfo(user.getFirstName(), user.getLastName());

		// Notify the number of book available.
		welcomePhraseNotification(user);

		// Filling the categories in the grid.
		LibraryAsynchrone library = proxyModel.getLibrary();

		List<String> categories = library.getCategories();
		Map<String, String> descriptions = library.getDescriptions();
		ObservableList<Node> children = categoryGrid.getChildren();
		System.out.println("Size : " +categories.size());
		for (int i = 0; i < 8; ++i) {
			CategoryDescriptionModule categoryModule = ModuleLoader.getInstance().load(CategoryDescriptionModule.class);
			String category = categories.get(i);
			categoryModule.setInformations(category, descriptions.get(category));
			children.add(categoryModule.getView());
		}
	}

	private void welcomePhraseNotification(UserAsynchrone user) {
		int nbBooks = user.getNbBooks();
		String message = nbBooks > 0 ? "You have " + nbBooks + " books you can read." : "You can borrow 5 books !";
		NotificationsManager.notify("Welcome :", message, NotificationType.INFO);
	}

	private void loadWorkAroundFont() {
		screenTitle.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 52));
	}

	private void loadModules() {
		burgerMenuModule = ModuleLoader.getInstance().load(BurgerMenuModule.class);
		addBookModule = ModuleLoader.getInstance().load(AddBookModule.class);
		searchModule = ModuleLoader.getInstance().load(SearchModule.class);
		paneRoot.getChildren().add(addBookModule.getView());
		paneRoot.getChildren().add(burgerMenuModule.getView());
		paneRoot.getChildren().add(searchModule.getView());
		searchModule.getView().setLayoutX(372);
		searchModule.getView().setLayoutY(22);
		// Configuring the burgerMenuModule
		burgerMenuModule.setAddBookModule(addBookModule);
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
