package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

public class AlbumAlreadyExistsException extends EntityAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	private String albumName;

	private String message = "Album %s already exists";

	public AlbumAlreadyExistsException(String albumName) {
		this.albumName = albumName;
	}

	public String getMessage() {
		return String.format(message, albumName);
	}
}
