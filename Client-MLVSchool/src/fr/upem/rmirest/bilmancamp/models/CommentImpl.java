package fr.upem.rmirest.bilmancamp.models;


import fr.upem.rmirest.bilmancamp.interfaces.BookComment;

public class CommentImpl  implements BookComment {

	private static final long serialVersionUID = 1L;
	private final String authors;
	private final String comment;
	
	
	public CommentImpl(String auth,String comment) {

		this.authors = auth;
		this.comment = comment;
	}

	public String getAuthors() {
		return authors;
	}

	@Override
	public String getContent() {
		return comment;
	}

}
