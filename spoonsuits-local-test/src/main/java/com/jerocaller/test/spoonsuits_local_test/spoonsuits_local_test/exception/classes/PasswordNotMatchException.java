package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends BaseCustomException {

    public PasswordNotMatchException() {
        super("패스워드 불일치", HttpStatus.BAD_REQUEST);
    }
    
    public PasswordNotMatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
    
}
