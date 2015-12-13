package application.controllers;

import application.model.ProxyModel;

public interface Screen extends Module, RemoteTaskObserver {

	public void initializeWithDynamicContent(ProxyModel proxyModel);

	/**
	 * @return The current instance of {@link ProxyModel} used by the
	 *         {@link Screen}.
	 */
	public ProxyModel getProxyModel();

	/**
	 * Observer methods.
	 */
	default public void onBookAdded() {
	}
}
