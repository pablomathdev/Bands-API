package com.github.pablomathdev.presentation.v1.exception;



import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.pablomathdev.domain.exceptions.EntityRelationshipException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.AlbumAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.EntityAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;

@RestControllerAdvice
public class ControllerException extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(BandNotFoundException.class)
	public ResponseEntity<Object> handleBandNotFoundException(BandNotFoundException ex, WebRequest request) {


		ControllerErrorMessage errorMessage = ControllerErrorMessage
				.builder()
				.code(NOT_FOUND.value())
				.type(ErrorType.RESOURCE_NOT_FOUND.toString())
				.message(ex.getMessage())
				.detail("The provided band is invalid. Please provide a valid band.")
				.build();

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), NOT_FOUND);
	}

	@ExceptionHandler(GenreNotFoundException.class)
	public ResponseEntity<Object> handleGenreNotFoundException(GenreNotFoundException ex, WebRequest request) {

		ControllerErrorMessage errorMessage = null;

		if (request.getDescription(false).indexOf("bands") != -1) {
			errorMessage = ControllerErrorMessage
					.builder()
					.code(BAD_REQUEST.value())
					.type(ErrorType.INVALID_PARAM.toString())
					.message("Genre is invalid")
					.detail("The provided band genre is invalid. Please provide a valid genre.")
					.build();
			
			return new ResponseEntity<>(errorMessage, new HttpHeaders(), BAD_REQUEST);
		}
		
		if (request.getDescription(false).indexOf("albums") != -1) {

			 errorMessage = ControllerErrorMessage
					.builder()
					.code(BAD_REQUEST.value())
					.type(ErrorType.INVALID_PARAM.toString())
					.message("Genre is invalid")
					.detail("The provided album genre is invalid. Please provide a valid genre.")
					.build();
			 
			 return new ResponseEntity<>(errorMessage, new HttpHeaders(), BAD_REQUEST);
			
		}
		
		
		   errorMessage = ControllerErrorMessage
				    .builder()
				    .code(NOT_FOUND.value())
				    .type(ErrorType.RESOURCE_NOT_FOUND.toString())
				    .message(ex.getMessage())
				    .detail("The provided genre is invalid. Please provide a valid genre.")
				    .build();
		

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), NOT_FOUND);
	}

	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {

		ControllerErrorMessage errorMessage = null;
      
         
		if(ex instanceof BandAlreadyExistsException subEx) {
			errorMessage = ControllerErrorMessage
					.builder()
					.code(CONFLICT.value())
					.type(ErrorType.RESOURCE_ALREADY_EXISTS.toString())
					.message(ex.getMessage())
					.detail("Cannot create the given band because it already exists. Please choose a different band.")
					.build();
		}else if(ex instanceof GenreAlreadyExistsException subEx) {
			errorMessage = ControllerErrorMessage
					.builder()
					.code(CONFLICT.value())
					.type(ErrorType.RESOURCE_ALREADY_EXISTS.toString())
					.message(subEx.getMessage())
					.detail("Cannot create the given genre because it already exists. Please choose a different genre.")
					.build();
		}else if(ex instanceof AlbumAlreadyExistsException subEx) {
			errorMessage = ControllerErrorMessage
					.builder()
					.code(CONFLICT.value())
					.type(ErrorType.RESOURCE_ALREADY_EXISTS.toString())
					.message(subEx.getMessage())
					.detail("Cannot create the given album because it already exists. Please choose a other album.")
					.build();
		}
		

		

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), CONFLICT);
	}

	
	@ExceptionHandler(EntityRelationshipException.class)
	public ResponseEntity<Object> handleEntityRelationshipException(EntityRelationshipException ex,WebRequest request){
		
		
		ControllerErrorMessage errorMessage = ControllerErrorMessage
				    .builder()
				    .code(CONFLICT.value())
				    .type(ErrorType.RESOURCE_INTEGRITY_VIOLATION.toString())
				    .message(ex.getMessage())
				    .detail("the genre provided is currently associated with other entities.")
				    .build();
		

		return new ResponseEntity<>(errorMessage, new HttpHeaders(),CONFLICT);
		
		
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		BindingResult bindingResult = ex.getBindingResult();
		
		List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream().map((field)->{
			String message = messageSource.getMessage(field, LocaleContextHolder.getLocale());
			
			return FieldError.builder()
					.field(field.getField())
					.message(message)
					.build();
			
		}).collect(Collectors.toList());
		
		
		ControllerErrorMessage errorMessage = ControllerErrorMessage.builder()
				.code(status.value())
				.type(ErrorType.INVALID_ARGUMENT.toString())
				.message("Invalid argument")
				.detail("One or more fields are invalid")
				.fields(fieldErrors)
				.build();
		
		
				
		return new ResponseEntity<>(errorMessage, new HttpHeaders(),status);
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleNoCapturedException(Exception ex,WebRequest request){
		
		ControllerErrorMessage errorMessage = ControllerErrorMessage
			    .builder()
			    .code(INTERNAL_SERVER_ERROR.value())
			    .type(ErrorType.SERVER_ERROR.toString())
			    .message("An internal error occurred in the application.")
			    .build();
	

	    return new ResponseEntity<>(errorMessage, new HttpHeaders(),INTERNAL_SERVER_ERROR);
		
	}
	
}
