package com.github.pablomathdev.domain.exceptions.notFoundExceptions;

public class TrackNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public TrackNotFoundException(String message) {
		super(message);

	}

	public TrackNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
