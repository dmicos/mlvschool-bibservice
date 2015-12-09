package application.utils;

public class Constants {

	public static final String SECURITY_POLICY_PATH = "data/confFiles/sec.policy";
	
	
	/**
	 * Numerical constants.
	 */
	public static final int PASSWORD_LENGTH = 10;
	public static final int NAME_LENGTH = 10;
	public static final int CARDID_LENGTH = 5;
	public static final int LOGGING_LENGTH = Constants.NAME_LENGTH + 1 + Constants.CARDID_LENGTH;

	/**
	 * Modules.
	 */
	public static final String CONNECTION_SCREEN_MODULE = "/scenes/connectionScreen/connectionScreen.fxml";
	public static final String CONNECTION_SIGNUP_MODULE = "/scenes/connectionScreen/signUpModule.fxml";
	public static final String CONNECTION_LOGIN_MODULE = "/scenes/connectionScreen/logInModule.fxml";

	/**
	 * Font constants.
	 */
	public static final String SF_DISPLAY_REGULAR = "/fonts/SF-UI-Display-Regular.otf";
	public static final String SF_DISPLAY_LIGHT = "/fonts/SF-UI-Display-Light.otf";
	public static final String SF_DISPLAY_ULTRALIGHT = "/fonts/SF-UI-Display-Ultralight.otf";
	public static final String SF_DISPLAY_THIN = "/fonts/SF-UI-Display-Thin.otf";
	public static final String SF_TEXT_SEMIBOLD = "/fonts/SF-UI-Text-Semibold.otf";
	public static final String SF_TEXT_REGULAR = "/fonts/SF-UI-Text-Regular.otf";
	public static final String SF_TEXT_MEDIUM = "/fonts/SF-UI-Text-Medium.otf";
	public static final String SF_TEXT_LIGHT = "/fonts/SF-UI-Text-Light.otf";

}
