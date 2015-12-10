package application.utils;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class Transformations {

	
	public static Point2D toSceneCoordinates(Node tf, Point2D point) {
		return tf.localToScene(point);
	}

}
