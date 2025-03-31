package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.TokenDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    /**
     * <p>
     *     Access Token 만료 시 재발급을 위한 API
     * </p>
     * <p>
     *     Access Token 재발급을 위해선 Refresh Token이 쿠키에 담긴 채로
     *     요청을 해야 함.
     * </p>
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<RestResponse> getNewAccessToken(
        HttpServletRequest request
    ) {

        TokenDto tokenDto = tokenService
            .getNewAccessTokenByRefreshTokenInCookie(request);

        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("성공적으로 액세스 토큰을 생성하였습니다.")
            .data(tokenDto)
            .build()
            .toResponseEntity();
    }

}
