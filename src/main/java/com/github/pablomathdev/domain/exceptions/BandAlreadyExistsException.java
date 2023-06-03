package com.github.pablomathdev.domain.exceptions;

public class BandAlreadyExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	private String message = "This Band Already Exists";


	public BandAlreadyExistsException(Throwable cause) {
		super(cause);
	}
	

	public String getMessage() {
		return message;
	}
}
