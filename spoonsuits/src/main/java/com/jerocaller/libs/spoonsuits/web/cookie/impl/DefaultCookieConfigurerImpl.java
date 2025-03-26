package com.jerocaller.libs.spoonsuits.web.cookie.impl;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieConfigurer;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class DefaultCookieConfigurerImpl implements CookieConfigurer {

    @Override
    public void configureCookie(Cookie cookie) {

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);

    }

}
