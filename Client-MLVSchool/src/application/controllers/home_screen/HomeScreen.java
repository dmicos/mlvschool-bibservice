package application.controllers.home_screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskLauncher;
import application.controllers.Screen;
import application.model.BookAsynchrone;
import application.model.LibraryAsynchrone;
import application.model.ProxyModel;
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

	private static final String TITLES[] = new String[] { "The 5 best rated books", "The 5 most recent books",
			"The 5 most consulted books" };
	@FXML
	private Pane paneRoot;

	@FXML
	private Text screenTitle;

	@FXML
	private GridPane categoryGrid;

	/* Model fields controlled */
	private ProxyModel proxyModel;

	private List<BookSpinerModule> spiners;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadWorkAroundFont();
	}

	/**
	 * Notifies the current {@link Screen} it is now the main application
	 * screen.
	 */
	public void startHasMainScreen() {
		// Notify the number of book available.
		welcomePhraseNotification(proxyModel.getConnectedUser().getNbBooks());
		startSpinnerAnimations();
	}

	@Override
	public void initializeWithDynamicContent(ProxyModel proxyModel) {
		System.out.println("lOADING HOME all");
		this.proxyModel = Objects.requireNonNull(proxyModel);

		// Filling the categories in the grid.
		LibraryAsynchrone library = proxyModel.getLibrary();
		loadSpinerModule(library);
		loadCategoriesTileModule(library);
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

	@Override
	public void onBookAdded() {
		reloadSpinnerBooks();
	}

	/**
	 * Reloads book spinner on the top of the screen.
	 */
	private void reloadSpinnerBooks() {
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

	private void loadSpinerModule(LibraryAsynchrone library) {
		spiners = new ArrayList<>();
		List<List<BookAsynchrone>> lists = new ArrayList<>();
		lists.add(library.getBestBooks());
		lists.add(library.getMostRecentBooks());
		lists.add(library.getMostConsultedBooks());
		BookSpinerModule.initChaineSpinners(TITLES, lists, paneRoot, spiners);
	}

	/**
	 * Starting animation of the first spinner. Chaining with other on the
	 * completion.
	 */
	private void startSpinnerAnimations() {
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
		// Switching to the search screen.
		RemoteTaskLauncher.searchBooksByCategory(this, category);
		paneRoot.setMouseTransparent(true);
		// TODO print a loader circle.
	}
}
