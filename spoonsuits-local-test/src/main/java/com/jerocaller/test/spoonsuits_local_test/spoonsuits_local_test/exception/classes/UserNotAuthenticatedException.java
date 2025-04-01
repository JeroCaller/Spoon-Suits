package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class UserNotAuthenticatedException extends BaseCustomException {

    public UserNotAuthenticatedException() {
        super("미인증된 사용자입니다.", HttpStatus.UNAUTHORIZED);
    }

    public UserNotAuthenticatedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
