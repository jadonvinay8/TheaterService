package com.capgemini.TheaterService.exceptions;

import com.capgemini.TheaterService.beans.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<ErrorResponse> cityNotFound(CityNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse("No City was found with given id", "404",
				e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TheaterNotFoundException.class)
	public ResponseEntity<ErrorResponse> theaterNotFound(TheaterNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse("No theater was found with given id",
				"404", e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse("No entity was found with given id, try changing the id(s)",
				"404", e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ErrorResponse> invalidOperation(InvalidOperationException e) {
		return new ResponseEntity<>(new ErrorResponse("Invalid Operation", "400", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

//	@ExceptionHandler(AuthenticationException.class)
//	public ResponseEntity<ErrorResponse> unauthorized(AuthenticationException e) {
//		return new ResponseEntity<>(new ErrorResponse("Enter correct credentials to authenticate",
//				"401", e.getMessage()), HttpStatus.UNAUTHORIZED);
//	}
//
//	@ExceptionHandler(HttpClientErrorException.Forbidden.class)
//	public ResponseEntity<ErrorResponse> forbidden(HttpClientErrorException.Forbidden e) {
//		return new ResponseEntity<>(new ErrorResponse("You don't have permission to access this resource",
//				"403", e.getMessage()), HttpStatus.FORBIDDEN);
//	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> nullPointer(NullPointerException e) {
		return new ResponseEntity<>(new ErrorResponse("Nulls are not allowed", "400", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> invalidInputPayload(MethodArgumentNotValidException e) {
		return new ResponseEntity<>(new ErrorResponse("Invalid Payload Supplied, check for nulls/blanks",
				"400", e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> invalidInput(ConstraintViolationException e) {
		return new ResponseEntity<>(new ErrorResponse("Constraints violated, check for nulls/blanks",
				"400",  e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> nullPayload(HttpMessageNotReadableException e) {
		return new ResponseEntity<>(new ErrorResponse("Request Body not supplied",
				"400",  e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TheaterNameValidationFailedException.class)
	public ResponseEntity<ErrorResponse> invalidName(TheaterNameValidationFailedException e) {
		return new ResponseEntity<>(new ErrorResponse("Theater name already present in the area",
				"400",  e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<ErrorResponse> operationFailed(OperationFailedException e) {
		return new ResponseEntity<>(new ErrorResponse("Something went wrong in companion service",
				e.getMessage(), "500"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> anyUnhandledException(Exception e) {
		return new ResponseEntity<>(new ErrorResponse("Some unknown error occurred",
				"500",  e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}