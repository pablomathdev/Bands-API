package com.github.pablomathdev.domain.exceptions.notFoundExceptions;

public class AlbumNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public AlbumNotFoundException(String message) {
		super(message);

	}

	public AlbumNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
