package com.github.pablomathdev.domain.exceptions;

public class GenreNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public GenreNotFoundException(String message) {
		super(message);

	}

	public GenreNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
