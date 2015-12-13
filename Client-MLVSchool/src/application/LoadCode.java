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
import application.controllers.home_screen.AddBookModule;
import application.controllers.home_screen.BookSpinerModule;
import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.CategoryDescriptionModule;
import application.controllers.home_screen.HomeScreen;
import application.controllers.home_screen.SearchModule;
import application.controllers.research_screen.BookEntryModule;
import application.controllers.research_screen.CategoryButtonModule;
import application.controllers.research_screen.ResearchScreen;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.scene.Scene;

public class LoadCode {
	/**
	 * Loads every JavaFX Modules.
	 */
	static void loadModules() {
		ModuleLoader loader = ModuleLoader.getInstance();
		loader.registerFXMLLoader(ConnectionScreen.class, Constants.CONNECTION_SCREEN_MODULE);
		loader.registerFXMLLoader(SignUpModule.class, Constants.CONNECTION_SIGNUP_MODULE);
		loader.registerFXMLLoader(LogInModule.class, Constants.CONNECTION_LOGIN_MODULE);
		loader.registerFXMLLoader(HomeScreen.class, Constants.HOME_SCREEN_MODULE);
		loader.registerFXMLLoader(BurgerMenuModule.class, Constants.HOME_BURGER_MENU_MODULE);
		loader.registerFXMLLoader(AddBookModule.class, Constants.HOME_ADD_BOOK_MODULE);
		loader.registerFXMLLoader(CategoryDescriptionModule.class, Constants.HOME_CATEGORY_MODULE);
		loader.registerFXMLLoader(SearchModule.class, Constants.HOME_SEARCH_MODULE);
		loader.registerFXMLLoader(BookSpinerModule.class, Constants.HOME_SPINER_MODULE);
		loader.registerFXMLLoader(ResearchScreen.class, Constants.RESEARCH_SCREEN);
		loader.registerFXMLLoader(BookEntryModule.class, Constants.RESEARCH_BOOK_ENTRY_MODULE);
		loader.registerFXMLLoader(CategoryButtonModule.class, Constants.RESEARCH_CATEGORY_BUTTON);
	}

	/**
	 * Workaround for JavaFX SDK 8 : jdk8.0_u65 has a bug in exported font
	 * management.
	 */
	static void loadFonts() {
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
		fontManager.register(SF_TEXT_SEMIBOLD, 32);
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

	static void loadCSS(Scene scene, String cssPath) {
		scene.getStylesheets().add(ClientMLVSchool.class.getResource(cssPath).toExternalForm());
	}
}
