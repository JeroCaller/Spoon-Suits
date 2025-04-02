package com.jerocaller.libs.spoonsuits.web.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 *     JWT 기반 인증 구현 시 AuthenticationProvider를 구현할 인터페이스
 * </p>
 * <p>
 *     <code>DefaultJwtAuthenticationProviderImpl</code>이 이의 구현체이며,
 *     커스터마이징을 원할 시 이 인터페이스를 구현하거나 해당 구현체를 상속받아
 *     구현한 후 <code>@Primary</code>를 적용하면 됨.
 * </p>
 *
 * @see com.jerocaller.libs.spoonsuits.web.jwt.impl.DefaultJwtAuthenticationProviderImpl
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api">jjwt-api</a>
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl">jjwt-impl</a>
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson">jjwt-jackson</a>
 * @see <a href="https://github.com/jwtk/jjwt">jjwt github and docs</a>
 */
public interface JwtAuthenticationProvider {

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";

    /**
     * <p>JWT 토큰 생성 메서드</p>
     *
     * @param user 사용자 정보가 담긴 UserDetails 구현체
     * @param expirationInMilliSeconds JWT 만료 시간(ms)
     * @return 사용자 정보가 담긴 문자열 형태의 JWT
     * @see #createAccessToken(UserDetails)
     * @see #createRefreshToken(UserDetails)
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
     * @return <code>SecurityContextHolder.getContext.setAuthentication()</code>
     * 메서드의 인자로 넘길 Authentication 구현체를 반환한다.
     */
    default Authentication getAuthentication(String token) {
        return null;
    }

    /**
     * <p>Access Token 생성 메서드</p>
     * <p>
     *     기존의 {@link #createToken(UserDetails, long)} 메서드보다는
     *     Access Token을 생성한다는 의미를 명확히 하고자 할 때 이 메서드를 구현한다.
     *     Access Token의 만료 시간은 {@link JwtProperties}를 이용하거나
     *     별도로 정한다.
     * </p>
     * <p>
     *     실질적인 토큰 생성 로직은
     *     내부에 {@link #createToken(UserDetails, long)}
     *     메서드를 호출하도록 구성할 수 있음.
     * </p>
     *
     * @param user
     * @return 문자열 형태의 새로 생성된 Access Token
     * @see #createToken(UserDetails, long)
     * @see JwtProperties
     */
    default String createAccessToken(UserDetails user) {
        return null;
    }

    /**
     * <p>Refresh Token 생성 메서드</p>
     * <p>
     *     기존의 {@link #createToken(UserDetails, long)} 메서드보다는
     *     Refresh Token을 생성한다는 의미를 명확히 하고자 할 때 이 메서드를 구현한다.
     *     Refresh Token의 만료 시간은 {@link JwtProperties}를 이용하거나
     *     별도로 정한다.
     * </p>
     * <p>
     *     실질적인 토큰 생성 로직은
     *     내부에 {@link #createToken(UserDetails, long)} 메서드를
     *     호출하도록 구성할 수 있음.
     * </p>
     *
     * @param user
     * @return 문자열 형태의 새로 생성된 Refresh Token
     * @see #createToken(UserDetails, long)
     * @see JwtProperties
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

    /**
     * <p>
     *     JWT로부터 모든 클레임들을 추출하기 위한 메서드.
     * </p>
     *
     * @param token 추출하고자 하는 JWT 토큰
     * @return 추출된 모든 클레임
     */
    default Claims extractClaims(String token) {
        return null;
    }

    /**
     * <p>
     *     JWT로부터 유저 네임을 추출하기 위한 메서드.
     * </p>
     * <p>
     *     유저 네임이 subject에 포함될 때에만 사용 가능.
     * </p>
     *
     * @param token username을 추출하고자 하는 JWT 토큰
     * @return username
     */
    default String extractUsernameFromToken(String token) {
        return null;
    }

}
