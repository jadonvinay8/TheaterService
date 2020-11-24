package com.capgemini.TheaterService.exceptions;

import com.capgemini.TheaterService.beans.ErrorResponse;
import com.capgemini.TheaterService.dto.MicroserviceResponse;
import com.capgemini.TheaterService.utils.ResponseBuilder;
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
	public ResponseEntity<MicroserviceResponse> cityNotFound(CityNotFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse("No City was found with given id", e.getMessage(), "404");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NOT_FOUND.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}
	
	@ExceptionHandler(TheaterNotFoundException.class)
	public ResponseEntity<MicroserviceResponse> theaterNotFound(TheaterNotFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse("No theater was found with given id", e.getMessage(), "404");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NOT_FOUND.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<MicroserviceResponse> entityNotFound(EntityNotFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse("No entity was found with given id", e.getMessage(), "404");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NOT_FOUND.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<MicroserviceResponse> invalidOperation(InvalidOperationException e) {
		ErrorResponse errorResponse = new ErrorResponse("Invalid Operation", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<MicroserviceResponse> nullPointer(NullPointerException e) {
		ErrorResponse errorResponse = new ErrorResponse("Nulls are not allowed", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MicroserviceResponse> invalidInputPayload(MethodArgumentNotValidException e) {
		ErrorResponse errorResponse = new ErrorResponse("Invalid Payload Supplied, check for nulls/blanks", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<MicroserviceResponse> invalidInput(ConstraintViolationException e) {
		ErrorResponse errorResponse = new ErrorResponse("Constraints violated, check for nulls/blanks", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MicroserviceResponse> nullPayload(HttpMessageNotReadableException e) {
		ErrorResponse errorResponse = new ErrorResponse("Invalid request body supplied", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(TheaterNameValidationFailedException.class)
	public ResponseEntity<MicroserviceResponse> invalidName(TheaterNameValidationFailedException e) {
		ErrorResponse errorResponse = new ErrorResponse("Theater name already present in the area", e.getMessage(), "400");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(MicroserviceException.class)
	public ResponseEntity<MicroserviceResponse> serviceCallException(MicroserviceException e) {
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.BAD_REQUEST.value(), null, e.getErrorResponse());
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<MicroserviceResponse> operationFailed(OperationFailedException e) {
		ErrorResponse errorResponse = new ErrorResponse("Something went wrong in companion service", e.getMessage(), "500");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MicroserviceResponse> anyUnhandledException(Exception e) {
		ErrorResponse errorResponse = new ErrorResponse("Some unknown error occurred", e.getMessage(), "500");
		MicroserviceResponse response = ResponseBuilder.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorResponse);
		return ResponseEntity.ok(response);
	}

}