package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.unittest;

import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class JwtPropertiesTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("JwtPropertiesTest 테스트 클래스 실행 여부 테스트")
    void initTest() {
        log.info("이 메시지가 보이면 실행에 문제가 없다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("JwtProperties application.yml에 있는 속성값 바인딩 테스트")
    void jwtPropertiesTest() {

        assertThat(jwtProperties.getIssuer()).isEqualTo("kimquel@good");
        assertThat(jwtProperties.getSecretKey())
            .isEqualTo("this-is-for-jwt-secret-key-which-has-very-long-length");
        assertThat(jwtProperties.getToken().getAccess().getExpiry().toMinutes())
            .isEqualTo(5);
        assertThat(jwtProperties.getToken().getAccess().getCookieName())
            .isEqualTo("access-token");
        assertThat(jwtProperties.getToken().getRefresh().getExpiry().toMinutes())
            .isEqualTo(10);
        assertThat(jwtProperties.getToken().getRefresh().getCookieName())
            .isEqualTo("refresh-token");

        log.info(jwtProperties.toString());

    }

}
