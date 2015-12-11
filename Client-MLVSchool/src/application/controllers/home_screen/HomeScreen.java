package application.controllers.home_screen;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.model.ProxyModel;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import fr.upem.rmirest.bilmancamp.interfaces.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class HomeScreen implements Initializable, Screen {

	@FXML
	private Pane paneRoot;

	/* BurgerMenu module on the left of the screen */
	private BurgerMenuModule burgerMenuModule;

	/* Model fields controlled */
	private User user;
	private ProxyModel proxyModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		burgerMenuModule = ModuleLoader.getInstance().load(BurgerMenuModule.class);
		paneRoot.getChildren().add(burgerMenuModule.getView());
	}

	@Override
	public void startHasMainScreen(User user, ProxyModel proxyModel) {
		this.user = Objects.requireNonNull(user);
		this.proxyModel = Objects.requireNonNull(proxyModel);
		// TODO pull this informations. And say "How about new books ?" if books
		// == 0.
		NotificationsManager.notify("News :", "You have 2 Books ready to read.", NotificationType.INFO);
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
	public User getUser() {
		return user;
	}
}
