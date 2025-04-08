package com.jerocaller.libs.spoonsuits.web.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 *     JWT Authentication Filter 구현체
 * </p>
 * <p>
 *     내부적으로 {@link JwtAuthenticationProvider}
 *     (기본적으로는 {@link com.jerocaller.libs.spoonsuits.web.jwt.impl.DefaultJwtAuthenticationProviderImpl}
 *     을 사용함. 해당 인터페이스를 구현하면 그 구현체를 사용함)를 의존성 주입받아
 *     사용하며,
 *     HTTP request의 header로부터 access token을 추출, 유효성 검사 후, 유효하면
 *     JWT에 담긴 사용자 정보를 추출,
 *     {@link SecurityContextHolder}에 {@link Authentication} 타입으로 사용자
 *     정보를 저장함.
 * </p>
 * <p>
 *     Refresh token에 대해선 다루지 않으며, 만약 HTTP request의 header 내에
 *     JWT가 없거나 유효하지 않은 경우 미인증으로 처리됨.
 * </p>
 * <p>
 *     이 filter를 SecurityFilterChain에 등록하려면 Spring Security
 *     설정 클래스에 다음과 같이 적용하면 된다.
 * </p>
 * <pre>{@code
 * @Configuration
 * @EnableWebSecurity
 * @RequiredArgsConstructor
 * public class SecurityConfig {
 *
 *     // ...
 *     private final DefaultJwtAuthenticationFilter defaultJwtAuthenticationFilter;
 *
 *     @Bean
 *     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
 *         throws Exception
 *     {
 *         httpSecurity
 *
 *             // ...
 *
 *             .addFilterBefore(
 *                 defaultJwtAuthenticationFilter,
 *                 UsernamePasswordAuthenticationFilter.class
 *             )
 *
 *             // ...
 *
 *         return httpSecurity.build();
 *     }
 *
 *     // ...
 * }
 * }</pre>
 */
@Component
@RequiredArgsConstructor
public class DefaultJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtAuthenticationProvider.resolveToken(request);

        if (accessToken != null &&
            jwtAuthenticationProvider.validateToken(accessToken)) {
            Authentication auth = jwtAuthenticationProvider
                .getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);

    }

}
