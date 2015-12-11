package fr.upem.rmirest.bilmancamp.helpers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import fr.upem.rmirest.bilmancamp.interfaces.Image;
import fr.upem.rmirest.bilmancamp.models.RealImage;

public class ImageHelper {

	/***
	 * When no images has been given by user, we set a default image
	 */
	private static final Path DEFAULT_IMAGE_PATH = Paths.get("/", "Server", "rsc", "icons");
	public static final RealImage DEFAULT_IMAGE = ImageHelper.load(DEFAULT_IMAGE_PATH);

	/**
	 * load an image located at given location
	 * 
	 * @return
	 */
	public static RealImage load(Path path) {

		return new RealImage(null) {
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
	public static List<RealImage> loadImage(List<Path> paths) {
		return paths.stream().map(path -> load(path)).collect(Collectors.toList());
	}

	public static RealImage createRealImage(Image image){
		return new RealImage(image.getPath());//debug
	}
}
