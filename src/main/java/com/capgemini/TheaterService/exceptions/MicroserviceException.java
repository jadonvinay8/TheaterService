package com.capgemini.TheaterService.exceptions;

import com.capgemini.TheaterService.beans.ErrorResponse;

public class MicroserviceException extends RuntimeException{

    private ErrorResponse errorResponse;

    public MicroserviceException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

}
