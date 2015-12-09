package application.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ControllerDynamicLimiters {

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

	public static void setNumericConstraint(final TextField tf) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					try {
						int value = Integer.parseInt(newValue);
						tf.setText(Integer.toString(value));
						return;
					} catch (Exception e) {
						return;
					}
				}
				tf.setText(oldValue);
			}
		});
	}

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
}
