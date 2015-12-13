package application.controllers.home_screen;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.ClientMLVSchool;
import application.controllers.Module;
import application.controllers.ModuleLoader;
import application.controllers.RemoteTaskLauncher;
import application.controllers.RemoteTaskObserver;
import application.controllers.research_screen.ResearchScreen;
import application.model.BookAsynchrone;
import application.model.ProxyModel;
import application.utils.Constants;
import application.utils.FontManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SearchModule implements Initializable, Module, RemoteTaskObserver {

	@FXML
	private HBox paneRoot;

	@FXML
	private ChoiceBox<String> choiceBox;

	@FXML
	private TextField textField;

	private ProxyModel proxyModel;

	private State state = State.HIDLE;

	private enum State {
		SEARCH_REQUESTED, HIDLE;
	}

	@Override
	public Pane getView() {
		return paneRoot;
	}

	public void searchRequested() {
		if (state == State.SEARCH_REQUESTED) {
			return;
		}
		String input = textField.getText();
		if (input.isEmpty() || input.matches("\\s+")) {
			return;
		}
		state = State.SEARCH_REQUESTED;
		String keywords[] = Arrays.stream(input.split("\\s+")).filter(s -> !s.isEmpty()).collect(Collectors.toList())
				.toArray(new String[0]);
		RemoteTaskLauncher.searchBooks(keywords, proxyModel, this);
	}

	@Override
	public void onBookFound(List<BookAsynchrone> books, String[] keywords) {
		state = State.HIDLE;
		ResearchScreen researchScreen = ModuleLoader.getInstance().load(ResearchScreen.class);
		ClientMLVSchool.getINSTANCE().setInstantNewScreen(researchScreen);
		researchScreen.loadBooks(Arrays.stream(keywords).collect(Collectors.joining(", ")), books);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textField.setFont(FontManager.getInstance().getFont(Constants.SF_TEXT_LIGHT, 18));
		textField.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				searchRequested();
			}
		});
	}

	public void setProxyModel(ProxyModel proxyModel) {
		this.proxyModel = proxyModel;
	}

	@Override
	public ProxyModel getProxyModel() {
		return proxyModel;
	}
}
