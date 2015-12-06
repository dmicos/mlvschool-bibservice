package fr.upem.rmirest.bilmancamp.interfaces;

import java.io.Serializable;

/**
 * Wraps JavaFX image. (Workaround for the moment, will be replaced by a real
 * JavaFX::Image).
 * 
 * @author Baxtalou
 *
 */
public interface Image extends Serializable {

	public String getPath();
}
