package com.capgemini.TheaterService.exceptions;

import com.capgemini.TheaterService.beans.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<ErrorResponse> cityNotFound(CityNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse("No Entity was found with given id", "404",
				e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TheaterNotFoundException.class)
	public ResponseEntity<ErrorResponse> theaterNotFound(TheaterNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse("No theater was found with given id",
				"404", e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ErrorResponse> invalidOperation(InvalidOperationException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage(), "400", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> anyUnhandledException(Exception e) {
		return new ResponseEntity<>(new ErrorResponse("Some unknown error occurred",
				"500",  e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}