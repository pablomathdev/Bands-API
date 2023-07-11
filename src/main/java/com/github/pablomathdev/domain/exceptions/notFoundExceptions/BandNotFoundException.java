package com.github.pablomathdev.domain.exceptions.notFoundExceptions;

public class BandNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public BandNotFoundException(String message) {
		super(message);

	}

	public BandNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
