package com.github.pablomathdev.domain.exceptions.notFoundExceptions;

public class SingleNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public SingleNotFoundException(String message) {
		super(message);

	}

	public SingleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
