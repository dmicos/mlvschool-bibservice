package application.controllers.home_screen;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.ModuleLoader;
import application.controllers.Screen;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.model.UserAsynchrone;
import application.utils.Constants;
import application.utils.FontManager;
import application.utils.ImageProcessors;
import application.utils.NotificationsManager;
import application.utils.NotificationsManager.NotificationType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HomeScreen implements Initializable, Screen {

	@FXML
	private Pane paneRoot;

	@FXML
	private Text screenTitle;

	/* BurgerMenu module on the left of the screen */
	private BurgerMenuModule burgerMenuModule;

	/* Model fields controlled */
	private ProxyModel proxyModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		burgerMenuModule = ModuleLoader.getInstance().load(BurgerMenuModule.class);
		Pane burgerMenuView = burgerMenuModule.getView();
		paneRoot.getChildren().add(burgerMenuView);
		screenTitle.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 52));
	}

	@Override
	public void startHasMainScreen(ProxyModel proxyModel) {
		this.proxyModel = Objects.requireNonNull(proxyModel);
		// Setting the user informations in the burgerMenu.
		UserAsynchrone user = Objects.requireNonNull(proxyModel.getConnectedUser());
		burgerMenuModule.setUserInfo(user.getFirstName(), user.getLastName());
		// Notify the number of book available.
		int nbBooks = user.getNbBooks();
		String message = nbBooks > 0 ? "You have " + nbBooks + " books you can read." : "You can borrow 5 books !";
		NotificationsManager.notify("Welcome :", message, NotificationType.INFO);
		try {
			ImageView v = new ImageView(ImageProcessors.decodeBase64(BookAsynchrone.BOOK));
			v.setLayoutX(104);
			v.setLayoutY(170);
			paneRoot.getChildren().add(v); // To add in a Paner in background of
											// the singer.
			paneRoot.getChildren().remove(burgerMenuModule.getView());
			paneRoot.getChildren().add(burgerMenuModule.getView());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("No contractuel available, juste put the delut action !");
			return;
		}
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
