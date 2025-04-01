package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 *     Spring Security가 불필요한 경우 이를 disable하기 위한
 *     테스트 설정 클래스.
 * </p>
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityDisableConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/test/**").permitAll()
            );

        return httpSecurity.build();
    }

}
