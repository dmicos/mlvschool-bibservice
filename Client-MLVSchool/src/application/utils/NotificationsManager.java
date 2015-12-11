package application.utils;

import application.ClientMLVSchool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

public class NotificationsManager {

	/**
	 * Types of notification, with embedded icons.
	 * 
	 * @author Jefferson
	 *
	 */
	public enum NotificationType {
		INFO(Constants.INFO_ICON), DATABASE(Constants.DATABASE_ICON);
		private final Image image;

		private NotificationType(String path) {
			image = new Image(ClientMLVSchool.class.getResource(path).toString());
		}

		Image getImage() {
			return image;
		}
	}

	private static final Duration DURATION = new Duration(2900);
	private static final Color COLOR = new Color(0.121f, 0.121f, 0.121f, 1);

	public static void notify(String title, String message, NotificationType type) {
		TrayNotification tray = new TrayNotification(title, message, type.image, COLOR);
		tray.setAnimationType(AnimationType.POPUP);
		tray.showAndDismiss(DURATION);
	}
}
