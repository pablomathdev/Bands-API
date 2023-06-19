package com.github.pablomathdev.presentation.v1.exception;

import java.time.OffsetDateTime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;


@JsonInclude(value = Include.NON_NULL)
@Getter
public class ControllerErrorMessage {

	private Integer code;
	private String type;
	private String message;
	private OffsetDateTime timestamp = OffsetDateTime.now();
	private String detail;
	List<FieldError> fields;
	

	public static Builder builder() {
		return new Builder();
	}

	
	public static class Builder {

		private ControllerErrorMessage controllerErrorMessage;

		private Builder() {
			controllerErrorMessage = new ControllerErrorMessage();
		}
		
		public Builder code(Integer code) {
			controllerErrorMessage.code = code;
			return this;
		}
		public Builder type(String type) {
			controllerErrorMessage.type = type;
			return this;
		}
		public Builder message(String message) {
			controllerErrorMessage.message = message;
			return this;
		}
		public Builder fields(List<FieldError> fields) {
			controllerErrorMessage.fields = fields;
			return this;
		}
		public Builder detail(String detail) {
			controllerErrorMessage.detail = detail;
			return this;
		}
		public ControllerErrorMessage build() {
			return controllerErrorMessage;
		}
	}
}
