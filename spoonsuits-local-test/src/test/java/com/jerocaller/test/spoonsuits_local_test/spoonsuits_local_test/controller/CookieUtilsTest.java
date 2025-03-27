package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.libs.spoonsuits.web.cookie.impl.DefaultCookieConfigurerImpl;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUser;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({CookieUtils.class, DefaultCookieConfigurerImpl.class})
@Slf4j
public class CookieUtilsTest {

    @Autowired
    private CookieUtils cookieUtils;

    @Test
    @DisplayName("테스트 클래스 작동 확인 테스트")
    void initTest() {
        log.info("=== {} 테스트 클래스 실행됨 ===", this.getClass().getName());
    }

    @Test
    @DisplayName("쿠키 직렬화 및 역직렬화 테스트")
    void serializationAndDeserializationTest()
        throws IOException, ClassNotFoundException
    {

        SiteUser mockUser = SiteUser.builder()
            .username("kimquel")
            .age(32)
            .build();
        String serializedUserInfo = cookieUtils.serialize(mockUser);

        Cookie mockCookie = new Cookie("USER", serializedUserInfo);
        mockCookie.setMaxAge(60 * 3);

        SiteUser actualUser = cookieUtils
            .deserialize(mockCookie, SiteUser.class);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(actualUser.getAge()).isEqualTo(mockUser.getAge());
        assertThat(actualUser).isEqualTo(mockUser);

        log.info("SiteUser info : {}", actualUser.getStringOfUserInfo());

    }

}
