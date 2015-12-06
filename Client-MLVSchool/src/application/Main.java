package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane screen = loadScreen("../scenes/connexionScreen/connexionScreen.fxml");
			Scene scene = new Scene(screen, 1200, 800);
			loadCSS(scene, "../css/florange.css");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			System.err.println("Cannot load the scene : " + e.getCause());
			return;
		}
	}

	private void loadCSS(Scene scene, String cssPath) {
		scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
	}

	private Pane loadScreen(String screenPath) throws IOException {
		return FXMLLoader.load(getClass().getResource(screenPath));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
