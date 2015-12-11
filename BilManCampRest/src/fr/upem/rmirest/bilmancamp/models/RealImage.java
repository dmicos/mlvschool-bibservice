package fr.upem.rmirest.bilmancamp.models;

import java.io.Serializable;

import fr.upem.rmirest.bilmancamp.interfaces.Image;

public class RealImage implements Serializable, Image {

	private static final long serialVersionUID = 1L;
	private String encodeValue;

	public RealImage(String base64) {
		encodeValue = base64;
	}

	// Required by WS
	public RealImage() {

	}

	@Override
	public String getPath() {
		return encodeValue;
	}

}
