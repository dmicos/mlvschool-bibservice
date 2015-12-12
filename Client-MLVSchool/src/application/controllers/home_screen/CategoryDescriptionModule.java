package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CategoryDescriptionModule implements Initializable, Module {

	@FXML
	private VBox paneRoot;

	@FXML
	private Label description;

	@FXML
	private Text title;

	private HomeScreen homeScreen;

	@FXML
	void paneRootClicked() {
		homeScreen.clickOnCategory(title.getText());
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FontManager fontManger = FontManager.getInstance();
		title.setFont(fontManger.getFont(Constants.SF_TEXT_SEMIBOLD, 24));
		description.setFont(fontManger.getFont(Constants.SF_TEXT_REGULAR, 20));
	}

	public void setInformations(String category, String description) {
		title.setText(category);
		this.description.setText(description);
	}

	public void setScreen(HomeScreen homeScreen) {
		this.homeScreen = homeScreen;
	}
}
