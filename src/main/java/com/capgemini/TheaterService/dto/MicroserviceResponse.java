package com.capgemini.TheaterService.dto;

import com.capgemini.TheaterService.beans.ErrorResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MicroserviceResponse {

    @Data
    @NoArgsConstructor
    public static class Payload {
        private Object response;
        private ErrorResponse exception;

        public Payload(Object response, ErrorResponse exception) {
            this.response = response;
            this.exception = exception;
        }
    }

    private int status;
    private Payload payload;

    public MicroserviceResponse(int status, Payload payload) {
        this.status = status;
        this.payload = payload;
    }
}
