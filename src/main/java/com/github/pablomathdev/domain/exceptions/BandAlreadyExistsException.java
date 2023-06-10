package com.github.pablomathdev.domain.exceptions;

public class BandAlreadyExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	private String bandName;

	private String message = "Band %s Already Exists!";

	public BandAlreadyExistsException(String bandName) {
		this.bandName = bandName;
	}

	public String getMessage() {
		return String.format(message, bandName);
	}
}
