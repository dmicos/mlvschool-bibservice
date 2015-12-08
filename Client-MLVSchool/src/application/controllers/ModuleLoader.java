package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class ModuleLoader {

	public static FXMLLoader loadModule(String path) {
		return new FXMLLoader(Main.class.getResource(path));
	}

	public static Pane getView(FXMLLoader loader) throws IOException {
		return (Pane) loader.load();
	}
}
