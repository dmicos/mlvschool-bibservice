package application.controllers;

import javafx.scene.layout.Pane;

/**
 * An instance of {@link Module} is a controller containing its own view,
 * representing a specific "part/module" of a "screen", or is a screen. For
 * example, the LoginModule controls the view and the data corresponding to the
 * login form inside the Connection screen, which is also a Module with its own
 * view.
 * 
 * @author Jefferson
 *
 */
public interface Module {

	/**
	 * Hides the current module. Either by an animation or directly. A module
	 * hided should not receive any inputs.
	 */
	public default void hide() {
		// Container like screen usually never implement intermediate
		// operations.
	}

	/**
	 * Shows the current module. Either by an animation or directly. A module
	 * shown should receive inputs correctly.
	 */
	public default void show() {
		// Container like screen usually never implement intermediate
		// operations.
	}

	/**
	 * 
	 * @return the view representing to the current module.
	 */
	public Pane getView();
}
