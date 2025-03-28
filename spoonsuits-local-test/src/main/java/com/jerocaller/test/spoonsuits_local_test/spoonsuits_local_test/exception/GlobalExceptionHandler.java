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

    /**
     * <p>
     *     ValidationUtils#getValidationFailedMessage() 메서드 테스트를 위한
     *     예외 핸들러.
     * </p>
     * <p>
     *     유효성 검사 실패 시 실패한 필드명과 그에 대한 메시지가 Map 형태로
     *     반환되는지 확인하기 위한 용도.
     * </p>
     *
     * @param e
     * @return
     */
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
