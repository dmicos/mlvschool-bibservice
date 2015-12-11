package fr.upem.rmirest.bilmancamp.helpers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import fr.upem.rmirest.bilmancamp.interfaces.Image;

public class ImageHelper {

	/***
	 * When no images has been given by user, we set a default image
	 */
	private static final Path DEFAULT_IMAGE_PATH = Paths.get("/", "Server", "rsc", "icons");
	public static final Image DEFAULT_IMAGE = ImageHelper.load(DEFAULT_IMAGE_PATH);

	/**
	 * load an image located at given location
	 * 
	 * @return
	 */
	public static Image load(Path path) {

		return new Image() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getPath() {
				return path.toString();
			}
		};
	}

	/**
	 * Load all image given as List of {@link Path}
	 * 
	 * @param paths
	 *            list of locations
	 * @return a list of {@link Image}
	 */
	public static List<Image> loadImage(List<Path> paths) {
		return paths.stream().map(path -> load(path)).collect(Collectors.toList());
	}

}
