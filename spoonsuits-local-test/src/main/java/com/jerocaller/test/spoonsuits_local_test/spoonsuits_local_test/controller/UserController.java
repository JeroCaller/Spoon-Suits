package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/users")
public class UserController {

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
