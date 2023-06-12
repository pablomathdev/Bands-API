package com.github.pablomathdev.domain.exceptions;

public class GenreAlreadyExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	private String genreName;

	private String message = "Genre %s Already Exists!";

	public GenreAlreadyExistsException(String genreName) {
		this.genreName = genreName;
	}

	public String getMessage() {
		return String.format(message, genreName);
	}
}
