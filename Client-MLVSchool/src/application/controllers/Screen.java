package application.controllers;

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
}
