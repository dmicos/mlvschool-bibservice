package fr.upem.rmirest.bilmancamp.modelloaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.upem.rmirest.bilmancamp.models.BookPOJO;
import fr.upem.rmirest.bilmancamp.models.UserPOJO;

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

	/**
	 * Get the initial categories of the database.
	 * 
	 * @return a list of the allowed categories.
	 */
	public Map<String, String> getCategories() {
		return Collections.unmodifiableMap(categories);
	}

	/**
	 * Get the initial user's status that are allowed.
	 * 
	 * @return a list of the allowed status.
	 */
	public List<String> getStatus() {
		return Collections.unmodifiableList(status);
	}

	/**
	 * Load a list of {@link UserPOJO}s from a json file that will be used to
	 * fill the database with initial content.
	 * 
	 * @param userFilePath
	 *            the json file's path.
	 * @return a list of dummy content.
	 * @throws JsonProcessingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<UserPOJO> loadUsers(String userFilePath)
			throws JsonProcessingException, FileNotFoundException, IOException {

		// Initialize the json parsing objects and container list.
		List<UserPOJO> users = new ArrayList<>();
		BufferedReader reader = Files.newBufferedReader(Paths.get(userFilePath), Charset.forName("UTF-8"));
		JsonNode root = mapper.readTree(reader);
		JsonFactory factory = new JsonFactory();

		// Read the books.
		root.get("users").forEach(node -> {
			try {
				// Parse a book and add it to the list.
				UserWrapper readValue = mapper.readValue(factory.createParser(node.toString()), UserWrapper.class);
				users.add(readValue.toUserPOJO());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});

		// Return the list.
		return users;
	}

	/**
	 * Load a list of {@link BookPOJO}s from a json file that will be used to
	 * fill the database with initial content.
	 * 
	 * @param userFilePath
	 *            the json file's path.
	 * @return a list of dummy content.
	 * @throws JsonProcessingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<BookPOJO> loadBooks(String bookFilePath)
			throws JsonProcessingException, FileNotFoundException, IOException {

		// Initialize the json parsing objects and container list.
		List<BookPOJO> books = new ArrayList<>();
		BufferedReader reader = Files.newBufferedReader(Paths.get(bookFilePath), Charset.forName("UTF-8"));
		JsonNode root = mapper.readTree(reader);
		JsonFactory factory = new JsonFactory();

		// Read the books.
		root.get("books").forEach(node -> {
			try {
				// Parse a book and add it to the list.
				BookWrapper readValue = mapper.readValue(factory.createParser(node.toString()), BookWrapper.class);
				books.add(readValue.toBookPOJO());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});

		// Return the list.
		return books;
	}

	public static List<RateWrapper> loadRates(String rateFilePath)
			throws JsonProcessingException, FileNotFoundException, IOException {

		// Initializes the json parsing and the list.
		List<RateWrapper> rates = new ArrayList<>();
		BufferedReader reader = Files.newBufferedReader(Paths.get(rateFilePath), Charset.forName("UTF-8"));
		JsonNode root = mapper.readTree(reader);

		// Read the books.
		root.get("rates").forEach(node -> {
			// Parse a book and add it to the list.
			int user = node.get("user").asInt();
			int book = node.get("book").asInt();
			int rate = node.get("rate").asInt();
			RateWrapper readValue = new RateWrapper(user, book, rate);
			rates.add(readValue);
		});

		// Return the list.
		return rates;
	}

	public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, IOException {
		List<UserPOJO> users = loadUsers("data/confFiles/userset.json");
		System.out.println(users);
		System.out.println(users.size());

		List<BookPOJO> books = loadBooks("data/confFiles/bookset.json");
		System.out.println(books.size());

		List<RateWrapper> rates = loadRates("data/confFiles/rateset.json");
		System.out.println(rates);
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
