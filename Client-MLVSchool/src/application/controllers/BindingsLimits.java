package application.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Provides every functionalities to control bindings of various JAVAFX text
 * fields controls.
 * 
 * @author Jefferson
 *
 */
public class BindingsLimits {

	/**
	 * Sets the max length of a {@link TextField}.
	 * 
	 * @param tf
	 * @param maxLength
	 */
	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	/**
	 * Transforms a {@link TextField} in a numeric only prompt.
	 * 
	 * @param tf
	 */
	public static void setNumericConstraint(final TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[0-9]*")) {
					try {
						tf.setText(newValue);
						return;
					} catch (Exception e) {
						return;
					}
				}
				tf.setText(oldValue);
			}
		});
	}

	/**
	 * Transforms a {@link TextField} in an alphabetic only prompt.
	 * 
	 * @param tf
	 */
	public static void setAlphabeticConstraint(final TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[a-zA-Z]*")) {
					tf.setText(oldValue);
					return;
				}
			}
		});
	}

	/**
	 * Transforms a {@link TextField} in a login only prompt (lastname.cardID).
	 * 
	 * @param tf
	 */
	public static void setLoginConstraint(final TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[a-zA-Z]{0,10}\\.{0,1}[0-9]{0,5}")) {
					tf.setText(oldValue);
					return;
				}
			}
		});
	}

	/**
	 * Creates a string binding that contains the value of the given observable
	 * string converted to lowercase. See {@link String#toLowerCase()}.
	 * 
	 * Thanks to @https://github.com/lestard/.
	 */
	public static StringBinding toLowerCase(ObservableValue<String> text) {
		return Bindings.createStringBinding(() -> text.getValue() == null ? "" : text.getValue().toLowerCase(), text);
	}
}
