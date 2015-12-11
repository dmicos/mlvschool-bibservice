package application.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javafx.scene.image.Image;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Singleton able to process image files by encoding and decoding theme in
 * Base64.
 * 
 * @author Jefferson
 *
 */
public class ImageProcessors {

	private static final ImageProcessors INSTANCE = new ImageProcessors();
	private final BASE64Encoder encoder = new BASE64Encoder();
	private final BASE64Decoder decoder = new BASE64Decoder();

	/**
	 * Encodes an image at the URL in Universal Base64 type.
	 * 
	 * @param url
	 * @return the string created.
	 * @throws IOException
	 */
	public static String encodeBase64(String url) throws IOException {
		Path path = Paths.get(Objects.requireNonNull(url));
		return INSTANCE.encoder.encodeBuffer(Files.readAllBytes(path));
	}

	/**
	 * Creates a JavaFX {@link Image} from a Base64 string.
	 * 
	 * @param base64
	 * @return The image decoded.
	 * @throws IOException
	 */
	public static Image decodeBase64(String base64) throws IOException {
		Objects.requireNonNull(base64);
		byte[] decodeBuffer = INSTANCE.decoder.decodeBuffer(base64);
		return new Image(new ByteArrayInputStream(decodeBuffer));
	}
}



//try {
//	// Encode / Decode;
//	String s = Files.readAllLines(Paths.get("data/images/image64b2.txt"), Charset.defaultCharset()).stream()
//			.reduce("", String::concat);
//	// String s = ImageProcessors.encodeBase64("data/images/kama.jpg");
//	ImageView v = new ImageView(ImageProcessors.decodeBase64(s));
//	paneRoot.getChildren().add(v);
//	System.out.println(s);
//	// Files.write(Paths.get("./image64b2.txt"), s.getBytes());
//} catch (IOException e1) {
//	e1.printStackTrace();
//}