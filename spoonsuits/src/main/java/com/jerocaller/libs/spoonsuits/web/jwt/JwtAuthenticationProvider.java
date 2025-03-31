package com.jerocaller.libs.spoonsuits.web.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

// TODO : JWT 관련 기능 모두 구현 후 javadoc 작성 및 보강
// TODO : JWT 관련 기능 모두 구현 후 테스트하기

public interface JwtAuthenticationProvider {

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";

    /**
     * <p>JWT 토큰 생성 메서드</p>
     *
     * @param user 사용자 정보가 담긴 UserDetails 구현체
     * @param expirationInMilliSeconds JWT 만료 시간(ms)
     * @return 사용자 정보가 담긴 문자열 형태의 JWT
     */
    String createToken(UserDetails user, long expirationInMilliSeconds);

    /**
     * <p>요청 헤더에 담긴 JWT 토큰을 찾아 반환하는 메서드</p>
     * <p>
     *     Authorization: Bearer (JWT) 형태의 요청 헤더에서 JWT를 가져온다.
     * </p>
     *
     * @param request
     * @return JWT 토큰
     */
    String resolveToken(HttpServletRequest request);

    /**
     * <p>주어진 토큰의 유효성 검증 메서드</p>
     *
     * @param token
     * @return
     */
    boolean validateToken(String token);

    /**
     * <p>UsernamePasswordAuthenticationToken 반환 메서드</p>
     * <p>
     *     Provider가 아닌 Filter에 이 기능을 구현하는 경우도 있기에
     *     default로 선언하여 구현 여부를 선택사항으로 남김.
     * </p>
     *
     * @param token
     * @return
     */
    default Authentication getAuthentication(String token) {
        return null;
    }

    /**
     * <p>Access Token 생성 메서드</p>
     * <p>
     *     기존의 createToken() 메서드보다는 Access Token을 생성한다는
     *     의미를 명확히하고자 할 때 이 메서드를 구현한다.
     *     Access Token의 만료 시간은 별도로 정한다.
     * </p>
     * <p>
     *     실질적인 토큰 생성 로직은
     *     내부에 createToken() 메서드를 호출하도록 구성할 수 있음.
     * </p>
     *
     * @param user
     * @return 문자열 형태의 새로 생성된 Access Token
     */
    default String createAccessToken(UserDetails user) {
        return null;
    }

    /**
     * <p>Refresh Token 생성 메서드</p>
     * <p>
     *     기존의 createToken() 메서드보다는 Refresh Token을 생성한다는
     *     의미를 명확히하고자 할 때 이 메서드를 구현한다.
     *     Refresh Token의 만료 시간은 별도로 정한다.
     * </p>
     * <p>
     *     실질적인 토큰 생성 로직은
     *     내부에 createToken() 메서드를 호출하도록 구성할 수 있음.
     * </p>
     *
     * @param user
     * @return 문자열 형태의 새로 생성된 Refresh Token
     */
    default String createRefreshToken(UserDetails user) {
        return null;
    }

    /**
     * <p>쿠키에서도 JWT를 추출하고자 할 때 구현할 메서드</p>
     *
     * @param request
     * @param cookieName JWT가 담긴 쿠키 이름
     * @return JWT 토큰. 없으면 null을 반환.
     */
    default String resolveTokenInCookie(
        HttpServletRequest request,
        String cookieName
    ) {
        return null;
    }

    default Claims extractClaims(String token) {
        return null;
    }

    default String extractUsernameFromToken(String token) {
        return null;
    }

}
