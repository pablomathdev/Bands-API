package com.github.pablomathdev.presentation.v1.exception;

import lombok.Getter;

@Getter
public class FieldError {

	private String field;

	private String message;

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private FieldError fieldError;

		private Builder() {
			fieldError = new FieldError();
		}

		public Builder field(String field) {
			fieldError.field = field;
			return this;
		}

		public Builder message(String message) {
			fieldError.message = message;
			return this;
		}

		public FieldError build() {
			return fieldError;
		}
	}
}
