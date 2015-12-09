package application.view.animations;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {

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

}
