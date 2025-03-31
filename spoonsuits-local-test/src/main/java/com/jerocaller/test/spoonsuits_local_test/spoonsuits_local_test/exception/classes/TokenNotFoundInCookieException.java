package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundInCookieException extends BaseCustomException {

    public TokenNotFoundInCookieException() {
        super("토큰을 쿠키에서 찾자 못했습니다.", HttpStatus.NOT_FOUND);
    }

    public TokenNotFoundInCookieException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
