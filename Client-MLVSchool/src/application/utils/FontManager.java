package application.utils;

import java.util.HashMap;
import java.util.Objects;

import javafx.scene.text.Font;

/**
 * Since JAVAFX has a bug for exporting specified fonts in CSS (font-family) on
 * the JAVA jdk1.8_65. I have to build this font manager to assign manually all
 * fonts.
 * 
 * @author Jefferson
 *
 */
public class FontManager {
	// Singleton.
	private static final FontManager INSTANCE = new FontManager();
	// Name <> (Size <> Font).
	private final HashMap<String, HashMap<Double, Font>> fonts;

	private FontManager() {
		fonts = new HashMap<>();
	}

	/**
	 * 
	 * @return Single instance.
	 */
	public static FontManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Registers a font of size <code>size</code> into the current manager.
	 * 
	 * @param path
	 * @param size
	 */
	public void register(String path, double size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size can not be nul: " + size);
		}
		Objects.requireNonNull(path);
		// Looking if any instance of the font already exists.
		HashMap<Double, Font> instances = fonts.get(path);
		if (instances == null) {
			// Charging the map for this very font.
			HashMap<Double, Font> newInstance = new HashMap<Double, Font>();
			Font font = loadFont(path, size);
			newInstance.put(size, font);
			fonts.put(path, newInstance);
			return;
		}
		// Searching for an instance of size <size>.
		Font font = instances.get(size);
		if (font != null) {
			System.err.println("Warning in FontManager : Font : " + path + " - Size : " + size);
			return;
		}
		// Loading a new font.
		instances.put(size, loadFont(path, size));
	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @return the font if it is presented.
	 * @throws IllegalArgumentException
	 *             if the font does not exists.
	 */
	public Font getFont(String name, double size) {
		Objects.requireNonNull(name);
		HashMap<Double, Font> instances = fonts.get(name);
		if (instances == null) {
			throw new IllegalArgumentException("No font of name : " + name + " - size : " + size + " registered.");
		}
		Font font = instances.get(size);
		if (font == null) {
			throw new IllegalArgumentException("No font of name : " + name + " for size : " + size + " registered.");
		}
		return font;
	}

	private Font loadFont(String path, double size) {
		return Font.loadFont(getClass().getResource(path).toExternalForm(), size);
	}
}
