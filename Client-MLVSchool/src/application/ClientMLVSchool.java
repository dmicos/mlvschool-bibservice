package application;

import static application.utils.Constants.SEARCH_MODULE_X;
import static application.utils.Constants.SEARCH_MODULE_Y;

import java.rmi.RemoteException;
import java.util.List;

import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskObserver;
import application.controllers.Screen;
import application.controllers.connection_screen.ConnectionScreen;
import application.controllers.home_screen.AddBookModule;
import application.controllers.home_screen.BookViewerModule;
import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.HomeScreen;
import application.controllers.home_screen.SearchModule;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.utils.Animations;
import application.utils.Constants;
import fr.upem.rmirest.bilmancamp.interfaces.Book;
import fr.upem.rmirest.bilmancamp.interfaces.MailBox;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main JAVAFX client application. It has to extend {@link Application} to be
 * conform the the JAVAFX cycle activity.
 * 
 * @author Jefferson
 *
 */
public class ClientMLVSchool extends Application implements RemoteTaskObserver {

	public static void main(String[] args) {
		String codebase = "file:///Users/Baxtalou/Documents/Master2/REST2/ProjetRMIREST/bilious-octoprune/Server/src/";
		System.setProperty("java.security.policy", Constants.SECURITY_POLICY_PATH);
		System.setProperty("java.rmi.server.codebase", codebase);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		LoadCode.loadFonts();
		LoadCode.loadModules();
		launch(args);
	}

	/**
	 * The Application is a "stand alone" singleton. According to the JAVAFX's
	 * cycle activity.
	 */
	private static ClientMLVSchool INSTANCE;

	// Application graphic contents.
	private Scene scene;
	private Stage primaryStage;
	private Screen currentScreen;

	/* Modules on the top of other screens (exception the connection screen). */
	private BurgerMenuModule burgerMenuModule;
	private AddBookModule addBookModule;
	private SearchModule searchModule;
	private BookViewerModule bookViewerModule;

	private MailBox<Book> mailBox;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		// Loading the JavaFX connection screen.
		ConnectionScreen connectionScreen = loadConnectionScreen();
		scene = new Scene(connectionScreen.getView(), 1200, 800);
		ProxyModel proxyModel = new ProxyModel();
		connectionScreen.initializeWithDynamicContent(proxyModel);
		connectionScreen.startCient();
		currentScreen = connectionScreen;
		LoadCode.loadCSS(scene, "/css/florange.css"); // Custom CSS style sheet.
		LoadCode.loadCSS(scene, "/css/combobox.css"); // Custom CSS style sheet.
		primaryStage.setScene(scene);
	    primaryStage.setResizable(false);
		primaryStage.show();

		// Setting the first screen to start.
		INSTANCE = this;

		try {
			mailBox = new MailBoxImpl();
		} catch (RemoteException e) {
			throw new IllegalStateException("No mail box available to be contacted.");
		}
	}

	/**
	 * The application is a singleton by itself (JAVAFX's ways...)
	 */
	public static ClientMLVSchool getINSTANCE() {
		return INSTANCE;
	}

	/**
	 * Used to invoke environment windows when picking an image in the
	 * addBookModule. It should in default visibility, I know. But the
	 * refactoring involve some work on various paths in view's FXML files to
	 * link Controller classes. This is on my todo list thought.
	 */
	public Stage getStage() {
		return primaryStage;
	}

	/**
	 * Loads the application's first screen. And resets the model.
	 */
	public void reloadApplicationFirstScreen() {
		ConnectionScreen connectionScreen = loadConnectionScreen();
		Scene scene = INSTANCE.getScene();
		currentScreen = connectionScreen;
		scene.setRoot(connectionScreen.getView());
		connectionScreen.initializeWithDynamicContent(new ProxyModel());
		connectionScreen.startCient();
	}

	private void cacheRoot(Pane pane) {
		pane.setCache(true);
		pane.setCacheShape(true);
		pane.setCacheHint(CacheHint.SPEED);
	}

	private void loadSearchModule(ProxyModel proxyModel) {
		searchModule = ModuleLoader.getInstance().load(SearchModule.class);
		searchModule.getView().setLayoutX(SEARCH_MODULE_X);
		searchModule.getView().setLayoutY(SEARCH_MODULE_Y);
		searchModule.setProxyModel(proxyModel);
	}

	private void loadAddBookModule(HomeScreen homeScreen, List<String> categories) {
		addBookModule = ModuleLoader.getInstance().load(AddBookModule.class);
		addBookModule.setCategories(categories);
		addBookModule.setHomeScreen(homeScreen);
	}

	private void loadBookViewerModule() {
		bookViewerModule = ModuleLoader.getInstance().load(BookViewerModule.class);
	}

	private void loadBurgerMenuModule(ProxyModel proxyModel) {
		burgerMenuModule = ModuleLoader.getInstance().load(BurgerMenuModule.class);
		// Configuring the burgerMenuModule
		burgerMenuModule.setAddBookModule(addBookModule);
		// Setting the user informations in the burgerMenu.
		burgerMenuModule.setUserInfo(proxyModel);
	}

	/**
	 * Change the entire scene from the current layout, to the new
	 * <code>newLayout</code>.
	 */
	public <T extends Screen> T setInstantNewScreen(T newScreen) {
		// Initializing the newScreen with dynamic contents.
		newScreen.initializeWithDynamicContent(currentScreen.getProxyModel());
		Pane newView = newScreen.getView();
		scene.setRoot(newView);
		cacheRoot(newView);

		// Hiding modules
		burgerMenuModule.hide();
		addBookModule.hide();
		bookViewerModule.hide();

		// Adding modules to their new layout.
		ObservableList<Node> oldScreenChildren = currentScreen.getView().getChildren();
		ObservableList<Node> newScreenChildren = newView.getChildren();
		Pane bookViewerView = bookViewerModule.getView();
		Pane burgerView = burgerMenuModule.getView();
		Pane addBookView = addBookModule.getView();
		Pane searchView = searchModule.getView();
		oldScreenChildren.remove(burgerView);
		oldScreenChildren.remove(searchView);
		oldScreenChildren.remove(addBookView);
		oldScreenChildren.remove(bookViewerView);
		newScreenChildren.add(burgerView);
		newScreenChildren.add(searchView);
		newScreenChildren.add(addBookView);
		newScreenChildren.add(bookViewerView);

		currentScreen = newScreen;
		return newScreen;
	}

	@Override
	public void onBookVisualized(BookAsynchrone book) {
		burgerMenuModule.hide();
		addBookModule.hide();
		bookViewerModule.setData(currentScreen.getProxyModel(), book);
		bookViewerModule.show();
	}

	private Scene getScene() {
		return scene;
	}

	private static ConnectionScreen loadConnectionScreen() {
		return ModuleLoader.getInstance().load(ConnectionScreen.class);
	}

	@Override
	public void stop() throws Exception {
		currentScreen.getProxyModel().disconnectUser();
		mailBox = null;
		System.exit(0);
	}

	/**
	 * Transitions, after a delay <code>delay</code>, the entire scene from the
	 * current screen, to the new <code>newScreen</code>.
	 */
	public void translateToHomeScreen(double delay, HomeScreen newScreen) {
		ProxyModel proxyModel = currentScreen.getProxyModel();
		// Initializing the new screen model.
		newScreen.initializeWithDynamicContent(proxyModel);
		// Attaching externals modules to the new screen.
		loadSearchModule(proxyModel);
		loadAddBookModule(newScreen, proxyModel.getLibrary().getCategories());
		loadBurgerMenuModule(proxyModel);
		loadBookViewerModule();

		// Adding these 3 modules to the current home screen.
		Pane newView = newScreen.getView();
		newView.getChildren().addAll(INSTANCE.searchModule.getView(), INSTANCE.addBookModule.getView(),
				INSTANCE.bookViewerModule.getView(), INSTANCE.burgerMenuModule.getView());

		// The root is a VBox of the current and the new layout.
		Pane currentView = currentScreen.getView();
		VBox root = new VBox();
		Scene scene = INSTANCE.getScene();
		root.getChildren().addAll(newView, currentView);
		root.setTranslateY(-scene.getHeight());

		// Caching the animation for better performances.
		cacheRoot(root);
		scene.setRoot(root);

		// The translation animation.
		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800), root);
		translateTransition.setInterpolator(Interpolator.EASE_OUT);
		translateTransition.setToY(0);

		// Reseting the newLayout as the only root at the end of the sequence.
		Transition sequence = Animations.delay(delay, translateTransition);
		sequence.setOnFinished(e -> {
			root.getChildren().remove(newView);
			scene.setRoot(newView);
			newScreen.startHasMainScreen();
			currentScreen = newScreen;
		});
		sequence.play();
	}

	@Override
	public ProxyModel getProxyModel() {
		return currentScreen.getProxyModel();
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public MailBox<Book> getMailBox() {
		return mailBox;
	}
}
