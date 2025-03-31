package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class TokenNotValidException extends BaseCustomException {

    public TokenNotValidException() {
        super("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }

    public TokenNotValidException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
