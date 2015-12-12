package application.controllers.research_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class CategoryButtonModule implements Initializable, Module {

	@FXML
	private Button button;

	@FXML
	private Pane paneRoot;

	private String category;
	private ResearchScreen screen;

	@FXML
	void onButtonClicked() {
		// TODO requete recherche rmi.
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 24));
	}

	void initializeContent(ResearchScreen screen, String category) {
		this.screen = screen;
		this.category = category;
	}
}
