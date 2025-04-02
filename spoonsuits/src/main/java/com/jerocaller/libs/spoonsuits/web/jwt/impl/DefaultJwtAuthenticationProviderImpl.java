package com.jerocaller.libs.spoonsuits.web.jwt.impl;

import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     {@link JwtAuthenticationProvider}의 기본 구현체
 * </p>
 * <p>
 *     <code>application.yml(또는 .properties)</code>의 프로퍼티 값들을
 *     필드들에 바인딩한 {@link JwtProperties} 객체를 이용함
 * </p>
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class DefaultJwtAuthenticationProviderImpl implements
    JwtAuthenticationProvider
{

    protected final JwtProperties jwtProperties;
    protected Key secretKey;

    /**
     * <p>JWT secret key 생성</p>
     */
    @PostConstruct
    public void init() {

        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey()
            .getBytes(StandardCharsets.UTF_8));

    }

    /**
     * <p>JWT 토큰 생성 메서드</p>
     * <p>JWT 생성 구조)</p>
     * <pre><code>
     *     return Jwts.builder()
     *             .header()
     *             .type("JWT")
     *             .and()
     *             .subject(user.getUsername())
     *             .claims(roles)
     *             .issuedAt(new Date())
     *             .issuer(jwtProperties.getIssuer())
     *  // expiration = new java.util.Date(System.currentTimeMillis() + expirationInMilliSeconds)
     *             .expiration(expiration)
     *             .signWith(secretKey)
     *             .compact();
     * </code></pre>
     * <ul>
     *     <li>
     *         <code>roles</code>는 {@code List<String>} 타입.
     *     </li>
     *     <li>
     *         subject에 username이 들어가 {@link #extractUsernameFromToken(String)}
     *         메서드를 이용하여 손쉽게 username을 추출할 수 있음.
     *     </li>
     * </ul>
     *
     * @param user 사용자 정보가 담긴 UserDetails 구현체
     * @param expirationInMilliSeconds JWT 만료 시간(ms)
     * @return 생성된 JWT 토큰
     */
    @Override
    public String createToken(UserDetails user, long expirationInMilliSeconds) {

        Date expiration = new Date(
            System.currentTimeMillis() + expirationInMilliSeconds
        );

        MultiValueMap<String, String> roles =
            new LinkedMultiValueMap<>();
        roles.put(
            "roles",
            getRoleNamesFromGrantedAuthority(user.getAuthorities())
        );

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .subject(user.getUsername())
            .claims(roles)
            .issuedAt(new Date())
            .issuer(jwtProperties.getIssuer())
            .expiration(expiration)
            .signWith(secretKey)
            .compact();
    }

    /**
     * <p>
     *     HTTP request의 header
     *     <code>Authorization: Bearer </code>에 담긴 JWT를 추출, 반환
     * </p>
     * <p>
     *     쿠키를 이용하여 JWT를 운용하는 경우, 쿠키로부터 JWT값을 추출하고자 한다면
     *     {@link #resolveTokenInCookie(HttpServletRequest, String)}를
     *     사용하면 된다.
     * </p>
     *
     * @param request
     * @return
     */
    @Override
    public String resolveToken(HttpServletRequest request) {

        String token = null;
        String authHeader = request
            .getHeader(JwtAuthenticationProvider.AUTHORIZATION);

        if (authHeader != null &&
            authHeader.startsWith(JwtAuthenticationProvider.BEARER)
        ) {
            token = authHeader
                .substring(JwtAuthenticationProvider.BEARER.length());
        }
        return token;
    }

    /**
     * <p>
     *     JWT 토큰 유효성 검사 메서드.
     * </p>
     * <p>
     *     토큰의 만료 여부, 토큰 변조 여부 등으로 발생하는 예외는 별도로 처리하지
     *     않고 한꺼번에 false로 처리하기에,
     *     각 상황에 따른 예외 처리를 하고자 한다면 이 메서드를 오버라이딩하여 별도로
     *     구현해야 함.
     * </p>
     *
     * @param token
     * @return 유효 시 true, 만료 등의 이유로 유효하지 않으면 false
     */
    @Override
    public boolean validateToken(String token) {

        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>
     *     {@link org.springframework.security.core.context.SecurityContextHolder}에
     *     JWT 토큰으로부터 얻은 사용자 정보를
     *     {@link Authentication} 타입으로 저장하기 위해 JWT 토큰으로부터 추출한
     *     사용자 정보를 Authentication 객체로 반환하는 메서드.
     * </p>
     * <p>
     *     principal에는 String 타입의 username을 넣도록 함. 따라서
     *     {@link Authentication#getPrincipal()}의 반환값도 String 타입이다.
     * </p>
     *
     * @param token
     * @return
     */
    @Override
    public Authentication getAuthentication(String token) {

        String username = extractUsernameFromToken(token);
        Collection<SimpleGrantedAuthority> roles =
            getGrantedAuthoritiesFromRoleNames(
                (List<String>) extractClaims(token).get("roles")
            );

        return new UsernamePasswordAuthenticationToken(username, null, roles);
    }

    /**
     * <p>유저 정보를 통해 Access token을 생성하는 메서드.</p>
     * <p>
     *     내부적으로 {@link #createToken(UserDetails, long)}을 호출하여 토큰을 생성하며,
     *     토큰 만료 기간은 {@link JwtProperties}와 매핑되는 application.yml(.properties)
     *     의 <code>jwt.token.access.expiry</code>의 값으로 함.
     * </p>
     *
     * @param user
     * @return
     * @see JwtProperties
     */
    @Override
    public String createAccessToken(UserDetails user) {
        return createToken(user, jwtProperties.getToken()
            .getAccess()
            .getExpiry()
            .toMillis()
        );
    }

    /**
     * <p>유저 정보를 통해 Refresh token을 생성, 반환하는 메서드</p>
     * <p>
     *     내부적으로 {@link #createToken(UserDetails, long)}을 호출하여 토큰을 생성하며,
     *     토큰 만료 기간은 {@link JwtProperties}와 매핑되는 application.yml(.properties)
     *     의 <code>jwt.token.refresh.expiry</code>의 값으로 함.
     * </p>
     *
     * @param user
     * @return
     * @see JwtProperties
     */
    @Override
    public String createRefreshToken(UserDetails user) {
        return createToken(user, jwtProperties.getToken()
            .getRefresh()
            .getExpiry()
            .toMillis()
        );
    }

    /**
     * <p>
     *     쿠키에 JWT를 담은 경우, 해당 쿠키로부터 JWT를 추출, 반환하는 메서드.
     * </p>
     *
     * @param request
     * @param cookieName JWT가 담긴 쿠키 이름
     * @return
     */
    @Override
    public String resolveTokenInCookie(
        HttpServletRequest request,
        String cookieName
    ) {

        Cookie jwtCookie = WebUtils.getCookie(request, cookieName);
        if (jwtCookie == null) {
            return null;
        }
        return jwtCookie.getValue();
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * <p>JWT의 subject에 username이 있는 경우 이를 통해 username을 추출하는 메서드.</p>
     *
     * @param token username을 추출하고자 하는 JWT 토큰
     * @return
     */
    @Override
    public String extractUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * <p>
     *     {@code Collection<? extends GrantedAuthority>} 타입의 role들을
     *     {@code List<String>}으로 변환하는 메서드.
     * </p>
     *
     * @param collection
     * @return
     */
    private List<String> getRoleNamesFromGrantedAuthority(
        Collection<? extends GrantedAuthority> collection
    ) {

        List<String> roleNames = new ArrayList<>();
        collection.forEach(grantedAuthority -> {
            roleNames.add(grantedAuthority.getAuthority());
        });
        return roleNames;
    }

    /**
     * <p>
     *     {@code List<String>} 타입의 role들을
     *     {@code Collection<SimpleGrantedAuthority>} 타입으로 변환하는 메서드.
     * </p>
     *
     * @param roleNames
     * @return
     */
    private Collection<SimpleGrantedAuthority> getGrantedAuthoritiesFromRoleNames(
        List<String> roleNames
    ) {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roleNames.forEach(roleName -> {
            authorities.add(new SimpleGrantedAuthority(roleName));
        });
        return authorities;
    }

}
