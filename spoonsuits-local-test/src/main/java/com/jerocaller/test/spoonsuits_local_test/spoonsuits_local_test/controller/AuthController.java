package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieRequest;
import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberWithTokenResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;
    private final JwtProperties jwtProperties;

    @PostMapping
    public ResponseEntity<RestResponse> login(
        HttpServletResponse response,
        @RequestBody MemberLogin memberLogin
    ) {

        MemberWithTokenResponse memberResponse = authService.login(memberLogin);
        cookieUtils.addCookie(
            response,
            CookieRequest.builder()
                .cookieName(jwtProperties.getToken().getRefresh()
                    .getCookieName()
                )
                .cookieValue(memberResponse.getTokenDto().getRefreshToken())
                .build()
        );

        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("로그인 성공")
            .data(memberResponse)
            .build()
            .toResponseEntity();
    }

}
