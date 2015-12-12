package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LibraryImplDataLoader {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final Map<String, String> categories;
	private final List<String> status;

	private LibraryImplDataLoader() {
		super();
		this.categories = new HashMap<>();
		this.status = new ArrayList<>();
	}

	public static LibraryImplDataLoader createLoader(String configFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		LibraryImplDataLoader loader = new LibraryImplDataLoader();

		// Read the json file.
		JsonNode jsonRoot = mapper.readTree(new FileReader(configFilePath));
		// Parse the status.
		JsonNode statusNode = jsonRoot.get("status");
		statusNode.elements().forEachRemaining(node -> loader.status.add(node.asText()));
		// Parse the categories
		JsonNode categoriesNode = jsonRoot.get("categories");
		loader.categories.putAll(jsonStringToMap(categoriesNode.toString()));

		// Return the fully initialized object.
		return loader;
	}

	public Map<String, String> getCategories() {
		return Collections.unmodifiableMap(categories);
	}

	public List<String> getStatus() {
		return Collections.unmodifiableList(status);
	}

	/**
	 * Parses the {@link String} as Json to create a {@link Map} using Jackson
	 * API.
	 * 
	 * @param content
	 *            A string containing some json.
	 * @return a {@link Map} which contains Json values.
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static Map<String, String> jsonStringToMap(String content)
			throws JsonParseException, JsonMappingException, IOException {
		// Parse the server's request using Gson API.
		HashMap<String, String> map = mapper.readValue(content, new TypeReference<HashMap<String, String>>() {
		});
		return map;
	}

}
