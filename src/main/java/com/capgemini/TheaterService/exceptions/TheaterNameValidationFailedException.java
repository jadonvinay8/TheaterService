package com.capgemini.TheaterService.exceptions;

/**
 * Custom Exception to throw in case theater already exists for given name in the same area
 *
 * @author Vinay Pratap Singh
 *
 */
public class TheaterNameValidationFailedException extends RuntimeException {

    public TheaterNameValidationFailedException() {
    }

    public TheaterNameValidationFailedException(String s) {
        super(s);
    }
}
