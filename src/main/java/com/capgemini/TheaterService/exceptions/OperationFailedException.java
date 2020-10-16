package com.capgemini.TheaterService.exceptions;

public class OperationFailedException extends RuntimeException {

    public OperationFailedException() {}

    public OperationFailedException(String s) {
        super(s);
    }
}
