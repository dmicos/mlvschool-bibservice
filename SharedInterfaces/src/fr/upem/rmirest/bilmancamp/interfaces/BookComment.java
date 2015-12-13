package fr.upem.rmirest.bilmancamp.interfaces;

import java.io.Serializable;

public interface BookComment extends Serializable {

	public String getAuthors();
	public String getContent();
}
