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

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class DefaultJwtAuthenticationProviderImpl implements
    JwtAuthenticationProvider
{

    protected final JwtProperties jwtProperties;
    protected Key secretKey;

    @PostConstruct
    public void init() {

        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey()
            .getBytes(StandardCharsets.UTF_8));

    }

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

    @Override
    public boolean validateToken(String token) {

        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Authentication getAuthentication(String token) {

        String username = extractUsernameFromToken(token);
        Collection<SimpleGrantedAuthority> roles =
            getGrantedAuthoritiesFromRoleNames(
                (List<String>) extractClaims(token).get("roles")
            );

        return new UsernamePasswordAuthenticationToken(username, null, roles);
    }

    @Override
    public String createAccessToken(UserDetails user) {
        return createToken(user, jwtProperties.getToken()
            .getAccess()
            .getExpiry()
            .toMillis()
        );
    }

    @Override
    public String createRefreshToken(UserDetails user) {
        return createToken(user, jwtProperties.getToken()
            .getRefresh()
            .getExpiry()
            .toMillis()
        );
    }

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

    @Override
    public String extractUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    private List<String> getRoleNamesFromGrantedAuthority(
        Collection<? extends GrantedAuthority> collection
    ) {

        List<String> roleNames = new ArrayList<>();
        collection.forEach(grantedAuthority -> {
            roleNames.add(grantedAuthority.getAuthority());
        });
        return roleNames;
    }

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
