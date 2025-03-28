package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception;

import com.jerocaller.libs.spoonsuits.web.ValidationUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse> handleValidationException(
        MethodArgumentNotValidException e
    ) {

        Map<String, String> validationExceptionMessage = ValidationUtils
            .getValidationFailedMessage(e);

        return RestResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .message("유효하지 않은 데이터가 감지되었습니다.")
            .data(validationExceptionMessage)
            .build()
            .toResponseEntity();
    }

}
