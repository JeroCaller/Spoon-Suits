package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseCustomException extends RuntimeException {

    protected HttpStatus httpStatus = HttpStatus.OK;

    public BaseCustomException() {
        super();
    }

    public BaseCustomException(String message) {
        super(message);
    }

    public BaseCustomException(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public BaseCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
