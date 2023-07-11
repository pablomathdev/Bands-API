package com.github.pablomathdev.domain.exceptions.alreadyExistsException;

public class TrackAlreadyExistsException extends EntityAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	private String trackName;

	private String message = "Track %s already exists";

	public TrackAlreadyExistsException(String trackName) {
		this.trackName = trackName;
	}

	public String getMessage() {
		return String.format(message, trackName);
	}
}
