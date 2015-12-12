package application.controllers.home_screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import application.ClientMLVSchool;
import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskLauncher;
import application.controllers.Screen;
import application.controllers.research_screen.ResearchScreen;
import application.model.BookAsynchrone;
import application.model.LibraryAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HomeScreen implements Initializable, Screen {

	private static final int SEARCH_MODULE_Y = 22;

	private static final int SEARCH_MODULE_X = 372;

	private static final String TITLES[] = new String[] { "The 5 best rated books", "The 5 most recent books",
			"The 5 most consulted books" };
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

	private List<BookSpinerModule> spiners;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWorkAroundFont();
	}

	@Override
	public void startHasMainScreen() {
		// Notify the number of book available.
		welcomePhraseNotification(proxyModel.getConnectedUser().getNbBooks());
		startSpinnerAnimations();
	}

	public void initDynamicContent(ProxyModel proxyModel) {
		this.proxyModel = Objects.requireNonNull(proxyModel);
		UserAsynchrone user = Objects.requireNonNull(proxyModel.getConnectedUser());

		// Filling the categories in the grid.
		LibraryAsynchrone library = proxyModel.getLibrary();
		loadSpinerModule(library);
		loadModules(library, user);
	}

	private void welcomePhraseNotification(int nbBook) {
		String message = nbBook > 0 ? "You have " + nbBook + " books you can read." : "Enjoy your 5 free borrowing !";
		String title = nbBook > 0 ? "Welcome" : "Hey !";
		NotificationsManager.notify(title, message, NotificationType.INFO);
	}

	private void loadWorkAroundFont() {
		screenTitle.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 52));
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}

	void reloadSpinnerBooks() {
		LibraryAsynchrone library = proxyModel.getLibrary();
		List<List<BookAsynchrone>> lists = new ArrayList<>();
		lists.add(library.getBestBooks());
		lists.add(library.getMostRecentBooks());
		lists.add(library.getMostConsultedBooks());
		int i = 0;
		for (BookSpinerModule s : spiners) {
			try {
				s.initContent(lists.get(i), TITLES[i]);
			} catch (IOException e) {
				System.err.println("Spinner " + TITLES[i] + " not reloaded.");
			}
			i++;
		}
		BookSpinerModule.initChaineSpinners(TITLES, lists, paneRoot, spiners);
		System.out.println("Spinners reloaded.");
	}

	private void loadModules(LibraryAsynchrone library, UserAsynchrone user) {
		// Z order bottom-up.
		loadCategoriesTileModule(library);
		loadSearchModule();
		loadAddBookModule(library.getCategories());
		loadBurgerMenuModule(user);
	}

	private void loadSearchModule() {
		searchModule = ModuleLoader.getInstance().load(SearchModule.class);
		paneRoot.getChildren().add(searchModule.getView());
		searchModule.getView().setLayoutX(SEARCH_MODULE_X);
		searchModule.getView().setLayoutY(SEARCH_MODULE_Y);
	}

	private void loadAddBookModule(List<String> categories) {
		addBookModule = ModuleLoader.getInstance().load(AddBookModule.class);
		paneRoot.getChildren().add(addBookModule.getView());
		addBookModule.setCategories(categories);
		addBookModule.setScreen(this);
	}

	private void loadBurgerMenuModule(UserAsynchrone user) {
		burgerMenuModule = ModuleLoader.getInstance().load(BurgerMenuModule.class);
		// Configuring the burgerMenuModule
		burgerMenuModule.setAddBookModule(addBookModule);
		// Setting the user informations in the burgerMenu.
		burgerMenuModule.setUserInfo(user.getFirstName(), user.getLastName());
		paneRoot.getChildren().add(burgerMenuModule.getView());
	}

	private void loadSpinerModule(LibraryAsynchrone library) {
		spiners = new ArrayList<>();
		List<List<BookAsynchrone>> lists = new ArrayList<>();
		lists.add(library.getBestBooks());
		lists.add(library.getMostRecentBooks());
		lists.add(library.getMostConsultedBooks());
		BookSpinerModule.initChaineSpinners(TITLES, lists, paneRoot, spiners);
	}

	private void startSpinnerAnimations() {
		// Starting animation of the first spinner. Chaining with other on the
		// completion.
		spiners.get(0).show();
	}

	private void loadCategoriesTileModule(LibraryAsynchrone library) {
		List<String> categories = library.getCategories();
		Map<String, String> descriptions = library.getDescriptions();

		int lineSize = 4;
		for (int i = 0; i < 8; ++i) {
			CategoryDescriptionModule categoryModule = ModuleLoader.getInstance().load(CategoryDescriptionModule.class);
			categoryModule.setScreen(this);
			String category = categories.get(i);
			categoryModule.setInformations(category, descriptions.get(category));
			categoryGrid.add(categoryModule.getView(), i % lineSize, i / lineSize);
		}
	}

	public void clickOnCategory(String category) {
		System.out.println("HomeScreen : Searching books : Before");
		// Switching to the search screen.
		RemoteTaskLauncher.searchBooksByCategory(this, category);
		paneRoot.setDisable(true);
		burgerMenuModule.hide();
		// TODO print a loader circle.
	}

	/**
	 * Switching to the new research screen.
	 */
	public void researchBooksReady(String keywords, List<BookAsynchrone> books) {
		System.out.println("HomeScreen : Searching books : After");
		// Removing modules
		paneRoot.getChildren().remove(burgerMenuModule);
		paneRoot.getChildren().remove(searchModule);
		// Creating the research screen.
		ResearchScreen researchScreen = ModuleLoader.getInstance().load(ResearchScreen.class);
		researchScreen.initContent(proxyModel, keywords, books, burgerMenuModule, searchModule);
		ClientMLVSchool.setInstantNewScreen(this, researchScreen);
		System.out.println("HomeScreen : Searching books : After after");
	}
}
