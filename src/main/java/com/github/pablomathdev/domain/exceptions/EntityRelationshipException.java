package com.github.pablomathdev.domain.exceptions;



public class EntityRelationshipException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	
	public EntityRelationshipException(String message,Throwable cause) {
		super(message,cause);
	}

}
