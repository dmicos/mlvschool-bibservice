package application.controllers.home_screen;

import application.controllers.Module;
import application.model.ProxyModel;
import fr.upem.rmirest.bilmancamp.interfaces.User;

public interface Screen extends Module {

	/**
	 * Notifies the current {@link Screen} it is now the main application
	 * screen.
	 * 
	 * @param user
	 *            connected account.
	 * @param proxyModel
	 *            PROXY model used to interact with.
	 */
	public void startHasMainScreen(User user, ProxyModel proxyModel);

	/**
	 * @return The current instance of {@link ProxyModel} used by the
	 *         {@link Screen}.
	 */
	public ProxyModel getProxyModel();

	/**
	 * @return The current instance of {@link User} used by the {@link Screen}.
	 */
	public User getUser();
}
