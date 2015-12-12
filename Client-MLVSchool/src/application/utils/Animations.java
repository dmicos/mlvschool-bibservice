package application.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Animations {

	public static final Runnable noAction = () -> {};
	public static Transition transitionOpacityAnimation(Interpolator interpolation, double fromX, double fromY,
			double toX, double toY, double transDuration, double fadeDuration, double fromOpacity, double toOpacity,
			Node node, Runnable action) {
		TranslateTransition translation = new TranslateTransition(new Duration(transDuration), node);
		translation.setInterpolator(interpolation);
		translation.setFromX(fromX);
		translation.setFromY(fromY);
		translation.setToX(toX);
		translation.setToY(toY);

		FadeTransition fade = new FadeTransition(new Duration(fadeDuration), node);
		fade.setInterpolator(interpolation);
		fade.setFromValue(fromOpacity);
		fade.setToValue(toOpacity);

		ParallelTransition anim = new ParallelTransition(node, translation, fade);
		anim.setOnFinished(e -> action.run());

		return anim;
	}

	public static Transition delay(double delay, Transition wrapped) {
		PauseTransition pause = new PauseTransition(new Duration(delay));
		SequentialTransition sequence = new SequentialTransition(pause, wrapped);
		return sequence;
	}

	public static TranslateTransition transitionX(double fromX, double toX, float duration, Interpolator interpolator,
			Pane view) {
		TranslateTransition transition = new TranslateTransition(new Duration(duration), view);
		transition.setFromX(fromX);
		transition.setToX(toX);
		transition.setInterpolator(interpolator);
		return transition;
	}

	public static TranslateTransition transitionY(double fromY, double toY, float duration, Interpolator interpolator,
			Pane view) {
		TranslateTransition transition = new TranslateTransition(new Duration(duration), view);
		transition.setFromY(fromY);
		transition.setToY(toY);
		transition.setInterpolator(interpolator);
		return transition;
	}

	public static void playWrongRectangle(double x, double y, double width, double height, Pane parent) {
		playColorRectangle(x, y, width, height, parent, Color.RED, new Color(1, 0, 0, 0.2));
	}

	public static void playGoodRectangle(double x, double y, double width, double height, Pane parent) {
		playColorRectangle(x, y, width, height, parent, Color.GREEN, new Color(0, 1, 0, 0.2));
	}

	private static void playColorRectangle(double x, double y, double width, double height, Pane parent, Color color,
			Color innerColor) {
		Rectangle r = new Rectangle(x, y, width, height);
		r.setStroke(color);
		r.setStrokeWidth(4);
		r.setFill(innerColor);
		FadeTransition fade = new FadeTransition(new Duration(5), r);
		fade.setFromValue(0);
		fade.setToValue(1);
		FadeTransition fadeR = new FadeTransition(new Duration(700), r);
		fadeR.setFromValue(1);
		fadeR.setToValue(0);
		parent.getChildren().add(r);
		SequentialTransition trans = new SequentialTransition(fade, new PauseTransition(new Duration(500)), fadeR);
		trans.setOnFinished(e -> parent.getChildren().remove(r));
		trans.play();
	}
}
