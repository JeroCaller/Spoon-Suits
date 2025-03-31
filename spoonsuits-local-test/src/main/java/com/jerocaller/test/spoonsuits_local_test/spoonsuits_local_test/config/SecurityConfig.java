package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config;

import com.jerocaller.libs.spoonsuits.web.jwt.DefaultJwtAuthenticationFilter;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DefaultJwtAuthenticationFilter defaultJwtAuthenticationFilter;

    private final String[] permitAllRequestUris = {
        "/test/auth/**",
        "/test/cookie/**",
        "/test/users/**",
        "/test/tokens/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception {

        httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(login -> login.disable())
            .logout(logout -> logout.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllRequestUris).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                defaultJwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return httpSecurity.build();
    }

    /**
     * 테스트용 가짜 유저들을 반환.
     *
     * @return
     */
    @Bean
    public UserDetailsService mockUsers() {

        Member memberOne = Member.builder()
            .username("Kimquel")
            .password("kimquel1234")
            .roles(Arrays.asList("USER"))
            .build();
        Member memberTwo = Member.builder()
            .username("Jeongdb")
            .password("jeongdb1122")
            .roles(Arrays.asList("USER", "STAFF"))
            .build();
        Member memberThree = Member.builder()
            .username("Javas")
            .password("javas2233")
            .roles(Arrays.asList("USER", "STAFF", "ADMIN"))
            .build();

        return new InMemoryUserDetailsManager(memberOne, memberTwo, memberThree);
    }

}
