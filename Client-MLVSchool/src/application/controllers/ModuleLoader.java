package application.controllers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import application.ClientMLVSchool;
import javafx.fxml.FXMLLoader;

public class ModuleLoader {

	private static final ModuleLoader INSTANCE = new ModuleLoader();
	private final HashMap<Class<? extends Module>, Supplier<? extends Module>> map;

	private ModuleLoader() {
		map = new HashMap<>();
	}

	public static ModuleLoader getInstance() {
		return INSTANCE;
	}

	/**
	 * Registers a child {@code .class} of {@code Module} and the path to load
	 * it.
	 * 
	 * @param moduleClass
	 * @param path
	 */
	public void registerFXMLLoader(Class<? extends Module> moduleClass, String path) {
		Objects.requireNonNull(path);
		map.put(moduleClass, () -> {
			try {
				FXMLLoader loader = new FXMLLoader(ClientMLVSchool.class.getResource(path));
				loader.load();
				return loader.getController();
			} catch (IOException e) {
				throw new UncheckedIOException("Error when generating module : " + moduleClass + ' ' + path, e);
			}
		});
	}

	/**
	 * 
	 * @param moduleClass
	 * @return A module of the specified type.
	 */
	public <T extends Module> T load(Class<T> moduleClass) {
		Supplier<? extends Module> loader;
		if (null == (loader = map.get(moduleClass))) {
			throw new IllegalStateException("No loader for Module of class : " + moduleClass);
		}
		return moduleClass.cast(loader.get());
	}
}
