package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

import com.github.pablomathdev.domain.exceptions.BusinessException;

public class EntityAlreadyExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;


	public EntityAlreadyExistsException(String message) {
		super(message);
	}
	
	public EntityAlreadyExistsException() {}



	
}
