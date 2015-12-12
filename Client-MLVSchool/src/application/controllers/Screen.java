package application.controllers;

import application.controllers.home_screen.BurgerMenuModule;
import application.controllers.home_screen.SearchModule;
import application.model.ProxyModel;

public interface Screen extends Module {

	/**
	 * Notifies the current {@link Screen} it is now the main application
	 * screen.
	 */
	public void startHasMainScreen();

	/**
	 * @return The current instance of {@link ProxyModel} used by the
	 *         {@link Screen}.
	 */
	public ProxyModel getProxyModel();

	public SearchModule getSearchModule();

	public BurgerMenuModule getBurgerMenuModule();
}
