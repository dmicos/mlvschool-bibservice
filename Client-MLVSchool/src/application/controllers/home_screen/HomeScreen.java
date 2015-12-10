package application.controllers.home_screen;

import java.net.URL;
import java.util.ResourceBundle;

import application.controllers.Module;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class HomeScreen implements Initializable, Module {

	@FXML
	private Pane paneRoot;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

}
