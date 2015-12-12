package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SearchModule implements Initializable, Module {

	@FXML
	private HBox paneRoot;

	@FXML
	private ChoiceBox<String> choiceBox;

	@FXML
	private TextField textField;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textField.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_LIGHT, 18));
	}
}
