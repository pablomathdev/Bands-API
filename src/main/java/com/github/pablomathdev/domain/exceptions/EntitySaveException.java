package com.github.pablomathdev.domain.exceptions;

public class EntitySaveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntitySaveException(String message, Throwable cause) {
		super(message, cause);
	}

}
