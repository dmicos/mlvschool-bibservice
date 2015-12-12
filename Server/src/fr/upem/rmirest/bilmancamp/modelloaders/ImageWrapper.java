package fr.upem.rmirest.bilmancamp.modelloaders;

import fr.upem.rmirest.bilmancamp.interfaces.Image;

/**
 * Implementation of {@link Image} used for json serialization.
 * 
 * @author qcampos
 *
 */
class ImageWrapper implements Image {

	private static final long serialVersionUID = -5875321548915503141L;
	private String data;

	public ImageWrapper() {
		// TODO Auto-generated constructor stub
	}

	public ImageWrapper(String data) {
		super();
		this.data = data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String getData() {
		return data;
	}

}