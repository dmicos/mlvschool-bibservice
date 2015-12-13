package application.controllers.home_screen;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import application.controllers.Module;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BookCommentModule implements Initializable, Module {

	@FXML
	private VBox paneRoot;

	@FXML
	private Label titleLabel;

	@FXML
	private Label contentLabel;

	@Override
	public Pane getView() {
		return paneRoot;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleLabel.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_SEMIBOLD, 18));
		contentLabel.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_REGULAR, 18));
	}

	public void setComment(String title, String content) {
		titleLabel.setText(Objects.requireNonNull(title));
		contentLabel.setText(Objects.requireNonNull(content));
	}
}
