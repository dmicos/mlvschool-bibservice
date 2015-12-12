package fr.upem.rmirest.bilmancamp.interfaces;

import java.io.Serializable;

/**
 * Wraps an Image encoded as a base64 {@link String}.
 * 
 * @author Baxtalou
 *
 */
public interface Image extends Serializable {

	/**
	 * @return the base64 string which represents the Image.
	 */
	public String getData();
}
