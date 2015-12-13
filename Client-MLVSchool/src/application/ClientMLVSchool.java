package application;

import java.util.List;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.controllers.connection_screen.ConnectionScreen;
import application.controllers.home_screen.AddBookModule;
import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.HomeScreen;
import application.controllers.home_screen.SearchModule;
import application.model.ProxyModel;
import application.utils.Animations;
import application.utils.Constants;
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
public class ClientMLVSchool extends Application {

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

	private static final int SEARCH_MODULE_Y = 22;
	private static final int SEARCH_MODULE_X = 372;

	/**
	 * The Application is a "stand alone" singleton. According to the JAVAFX
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
		primaryStage.show();

		// Setting the first screen to start.
		INSTANCE = this;
	}

	public static ClientMLVSchool getINSTANCE() {
		return INSTANCE;
	}

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

	private void cacheRoot(VBox root) {
		root.setCache(true);
		root.setCacheShape(true);
		root.setCacheHint(CacheHint.SPEED);
	}

	private void loadSearchModule() {
		searchModule = ModuleLoader.getInstance().load(SearchModule.class);
		searchModule.getView().setLayoutX(SEARCH_MODULE_X);
		searchModule.getView().setLayoutY(SEARCH_MODULE_Y);
	}

	private void loadAddBookModule(HomeScreen homeScreen, List<String> categories) {
		addBookModule = ModuleLoader.getInstance().load(AddBookModule.class);
		addBookModule.setCategories(categories);
		addBookModule.setHomeScreen(homeScreen);
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
	public <T extends Screen> T setInstantNewScreen(Screen currentScreen, T newScreen) {
		// Initializing the newScreen with dynamic contents.
		newScreen.initializeWithDynamicContent(currentScreen.getProxyModel());
		Pane newView = newScreen.getView();
		scene.setRoot(newView);
		newView.setCache(true);
		newView.setCacheShape(true);
		newView.setCacheHint(CacheHint.SPEED);

		// Hiding modules
		burgerMenuModule.hide();
		addBookModule.hide();

		// Adding modules to their new layout.
		ObservableList<Node> oldScreenChildren = currentScreen.getView().getChildren();
		ObservableList<Node> newScreenChildren = newView.getChildren();
		Pane burgerView = burgerMenuModule.getView();
		Pane addBookView = addBookModule.getView();
		Pane searchView = searchModule.getView();
		oldScreenChildren.remove(burgerView);
		oldScreenChildren.remove(searchView);
		oldScreenChildren.remove(addBookView);
		newScreenChildren.add(burgerView);
		newScreenChildren.add(searchView);
		newScreenChildren.add(addBookView);

		return newScreen;
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
		loadSearchModule();
		loadAddBookModule(newScreen, proxyModel.getLibrary().getCategories());
		loadBurgerMenuModule(proxyModel);

		// Adding these 3 modules to the current home screen.
		Pane newView = newScreen.getView();
		newView.getChildren().addAll(INSTANCE.searchModule.getView(), INSTANCE.addBookModule.getView(),
				INSTANCE.burgerMenuModule.getView());

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

	private Scene getScene() {
		return scene;
	}

	private static ConnectionScreen loadConnectionScreen() {
		return ModuleLoader.getInstance().load(ConnectionScreen.class);
	}

	@Override
	public void stop() throws Exception {
		currentScreen.getProxyModel().disconnectUser();
	}
}
