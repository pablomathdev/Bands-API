package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

public class GenreAlreadyExistsException extends EntityAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	private String genreName;

	private String message = "Genre %s already exists";

	public GenreAlreadyExistsException(String genreName) {
		this.genreName = genreName;
	}

	public String getMessage() {
		return String.format(message, genreName);
	}
}
