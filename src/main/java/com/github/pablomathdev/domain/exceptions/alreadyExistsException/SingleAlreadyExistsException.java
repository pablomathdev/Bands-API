package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

public class SingleAlreadyExistsException extends EntityAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	private String singleTitle;

	private String message = "Single %s already exists";

	public SingleAlreadyExistsException(String singleTitle) {
		this.singleTitle = singleTitle;
	}

	public String getMessage() {
		return String.format(message, singleTitle);
	}
}
