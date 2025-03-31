package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.TokenDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.TokenNotFoundInCookieException;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.TokenNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public TokenDto getNewTokens(UserDetails member) {

        String accessToken = jwtAuthenticationProvider
            .createAccessToken(member);
        String refreshToken = jwtAuthenticationProvider
            .createRefreshToken(member);
        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenDto getNewAccessTokenByRefreshTokenInCookie(
        HttpServletRequest request
    ) {

        String refreshToken = jwtAuthenticationProvider.resolveTokenInCookie(
            request,
            jwtProperties.getToken().getRefresh().getCookieName()
        );

        if (refreshToken == null) {
            throw new TokenNotFoundInCookieException();
        } else if (!jwtAuthenticationProvider.validateToken(refreshToken)) {
            throw new TokenNotValidException();
        }

        UserDetails member = userDetailsService
            .loadUserByUsername(jwtAuthenticationProvider
                .extractUsernameFromToken(refreshToken)
            );

        String newAccessToken = jwtAuthenticationProvider
            .createAccessToken(member);

        return TokenDto.builder()
            .accessToken(newAccessToken)
            .build();
    }

}
