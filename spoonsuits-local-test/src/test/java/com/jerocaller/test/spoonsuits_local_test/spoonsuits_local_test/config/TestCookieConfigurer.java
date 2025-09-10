package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieConfigurer;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

public class TestCookieConfigurer implements CookieConfigurer {

    @Override
    public void configureCookie(Cookie cookie) {

        cookie.setMaxAge(60 * 5);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setPath("/cookie/test");
        cookie.setAttribute("TEST_CONFIGURE", "This is for Test");

    }

}
