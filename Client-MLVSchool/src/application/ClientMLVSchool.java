package application;

import static application.utils.Constants.SF_DISPLAY_LIGHT;
import static application.utils.Constants.SF_DISPLAY_REGULAR;
import static application.utils.Constants.SF_DISPLAY_THIN;
import static application.utils.Constants.SF_DISPLAY_ULTRALIGHT;
import static application.utils.Constants.SF_TEXT_LIGHT;
import static application.utils.Constants.SF_TEXT_MEDIUM;
import static application.utils.Constants.SF_TEXT_REGULAR;
import static application.utils.Constants.SF_TEXT_SEMIBOLD;

import application.controllers.ModuleLoader;
import application.controllers.connection_screen.ConnectionScreen;
import application.controllers.connection_screen.LogInModule;
import application.controllers.connection_screen.SignUpModule;
import application.controllers.home_screen.HomeScreen;
import application.model.ProxyModel;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
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
		loadFonts();
		loadModules();
		launch(args);
	}

	private static ClientMLVSchool INSTANCE;
	private Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Loading the JavaFX connection screen.
		ConnectionScreen connectionScreen = loadConnectionScreen();
		scene = new Scene(connectionScreen.getView(), 1200, 800);
		connectionScreen.setModel(new ProxyModel());
		connectionScreen.startCient();
		loadCSS(scene, "/css/florange.css"); // Custom CSS style sheet.
		primaryStage.setScene(scene);
		primaryStage.show();

		// Setting the first screen to start.
		INSTANCE = this;
	}

	public static void reloadApplicationFirstScreen() {
		ConnectionScreen connectionScreen = loadConnectionScreen();
		Scene scene = INSTANCE.getScene();
		scene.setRoot(connectionScreen.getView());
		connectionScreen.setModel(new ProxyModel());
		connectionScreen.startCient();
	}

	/**
	 * Loads every JavaFX Modules.
	 */
	private static void loadModules() {
		ModuleLoader loader = ModuleLoader.getInstance();
		loader.registerFXMLLoader(ConnectionScreen.class, Constants.CONNECTION_SCREEN_MODULE);
		loader.registerFXMLLoader(SignUpModule.class, Constants.CONNECTION_SIGNUP_MODULE);
		loader.registerFXMLLoader(LogInModule.class, Constants.CONNECTION_LOGIN_MODULE);
		loader.registerFXMLLoader(HomeScreen.class, Constants.HOME_SCREEN_MODULE);
	}

	/**
	 * Workaround for JavaFX SDK 8 : jdk8.0_u65 has a bug in exported font
	 * management.
	 */
	private static void loadFonts() {
		FontManager fontManager = FontManager.getInstance();
		fontManager.register(SF_DISPLAY_REGULAR, 32);
		fontManager.register(SF_DISPLAY_REGULAR, 110);
		fontManager.register(SF_DISPLAY_LIGHT, 18);
		fontManager.register(SF_DISPLAY_LIGHT, 24);
		fontManager.register(SF_DISPLAY_LIGHT, 30);
		fontManager.register(SF_DISPLAY_ULTRALIGHT, 18);
		fontManager.register(SF_DISPLAY_ULTRALIGHT, 24);
		fontManager.register(SF_DISPLAY_THIN, 28);
		fontManager.register(SF_TEXT_SEMIBOLD, 18);
		fontManager.register(SF_TEXT_SEMIBOLD, 24);
		fontManager.register(SF_TEXT_SEMIBOLD, 36);
		fontManager.register(SF_TEXT_SEMIBOLD, 52);
		fontManager.register(SF_TEXT_REGULAR, 18);
		fontManager.register(SF_TEXT_REGULAR, 20);
		fontManager.register(SF_TEXT_REGULAR, 24);
		fontManager.register(SF_TEXT_REGULAR, 28);
		fontManager.register(SF_TEXT_MEDIUM, 20);
		fontManager.register(SF_TEXT_LIGHT, 18);
		fontManager.register(SF_TEXT_LIGHT, 24);
	}

	private void loadCSS(Scene scene, String cssPath) {
		scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
	}

	private Scene getScene() {
		return scene;
	}

	private static ConnectionScreen loadConnectionScreen() {
		return ModuleLoader.getInstance().load(ConnectionScreen.class);
	}

	public static void transitionToNewLayout(Parent currentLayout, Parent newLayout) {
		Scene scene = INSTANCE.getScene();
		VBox root = new VBox();
		root.getChildren().addAll(newLayout, currentLayout);
		root.setTranslateY(-scene.getHeight());
		root.setCache(true);
		root.setCacheShape(true);
		root.setCacheHint(CacheHint.SPEED); // Less visually "pleasant".
		scene.setRoot(root);
		scene.getRoot();
		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800), root);
		translateTransition.setInterpolator(Interpolator.EASE_OUT);
		translateTransition.setToY(0);
		translateTransition.play();
		// TODO set home as the main root.
	}
}
