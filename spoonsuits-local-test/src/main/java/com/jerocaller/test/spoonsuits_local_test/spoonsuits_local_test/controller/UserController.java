package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/users")
public class UserController {

    /**
     * <p>
     *     validationUtils#getValidationFailedMessage() 메서드 테스트를 위한 
     *     컨트롤러 메서드. 
     * </p>
     * <p>
     *     신규 유저 가입이란 상황을 가정하여 신규 유저 정보와 매핑될 
     *     SiteUserRegisterDto 객체에 적용된 유효성 검사 실패 시 
     *     실패한 필드명과 그 메시지를 생성하는지 보기 위함.
     * </p>
     * 
     * 
     * @see com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.GlobalExceptionHandler#handleValidationException(MethodArgumentNotValidException) 
     * @param registerDto
     * @return
     */
    @PostMapping
    public ResponseEntity<RestResponse> register(
        @Valid @RequestBody SiteUserRegisterDto registerDto
    ) {
        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("회원 가입 성공")
            .build()
            .toResponseEntity();
    }

}
