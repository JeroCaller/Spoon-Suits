package com.jerocaller.libs.spoonsuits.web.cookie.impl;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieConfigurer;
import jakarta.servlet.http.Cookie;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     기본 쿠키 설정 구현체.
 *     개발용으로 설정됨.
 * </p>
 *
 * <p>
 *     쿠키는 다음과 같이 설정되어 있음.
 * </p>
 * <pre><code>
 * cookie.setHttpOnly(true);
 * cookie.setPath("/");
 * cookie.setSecure(false);
 * </code></pre>
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultCookieConfigurerImpl implements CookieConfigurer {

    @Override
    public void configureCookie(Cookie cookie) {

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);

    }

}
