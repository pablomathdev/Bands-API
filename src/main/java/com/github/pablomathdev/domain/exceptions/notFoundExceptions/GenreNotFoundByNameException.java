package com.github.pablomathdev.domain.exceptions.notFoundExceptions;

public class GenreNotFoundByNameException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public GenreNotFoundByNameException(String message) {
		super(message);

	}


}
