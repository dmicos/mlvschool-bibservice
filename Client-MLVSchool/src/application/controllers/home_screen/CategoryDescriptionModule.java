package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
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

	@FXML
	void paneRootClicked() {
		System.out.println("PaneRootClicked");
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO load fonts here.
	}

	public void setInformations(String category, String description) {
		title.setText(category);
		this.description.setText(description);
	}

}
