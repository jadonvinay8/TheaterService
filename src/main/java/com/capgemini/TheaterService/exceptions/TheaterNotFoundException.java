package com.capgemini.TheaterService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception to throw in case no theater exist for given id
 * 
 * @author Vinay Pratap Singh
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TheaterNotFoundException extends RuntimeException {

	public TheaterNotFoundException() {
		
	}

	public TheaterNotFoundException(String arg0) {
		super(arg0);
	}

}
