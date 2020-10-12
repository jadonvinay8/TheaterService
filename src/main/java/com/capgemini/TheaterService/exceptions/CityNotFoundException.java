package com.capgemini.TheaterService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception to throw in case no city exist for given id
 * 
 * @author Vinay Pratap Singh
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CityNotFoundException() {
		
	}

	public CityNotFoundException(String arg0) {
		super(arg0);
	}

}
