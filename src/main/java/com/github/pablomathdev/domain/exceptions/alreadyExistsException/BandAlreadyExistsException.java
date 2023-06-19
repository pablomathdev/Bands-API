package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

public class BandAlreadyExistsException extends EntityAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	private String bandName;

	private String message = "Band %s already exists";

	public BandAlreadyExistsException(String bandName) {
		this.bandName = bandName;
	}

	public String getMessage() {
		return String.format(message, bandName);
	}
}
