package com.jerocaller.libs.spoonsuits.web.cookie;

import jakarta.servlet.http.Cookie;

@FunctionalInterface
public interface CookieConfigurer {

    void configureCookie(Cookie cookie);

}
