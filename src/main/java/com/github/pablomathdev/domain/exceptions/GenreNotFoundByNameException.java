package com.github.pablomathdev.domain.exceptions;

public class GenreNotFoundByNameException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public GenreNotFoundByNameException(String message) {
		super(message);

	}

	public GenreNotFoundByNameException() {
	}

}
