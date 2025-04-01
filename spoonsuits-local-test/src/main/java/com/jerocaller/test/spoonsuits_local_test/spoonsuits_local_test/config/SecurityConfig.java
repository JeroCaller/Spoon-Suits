package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config;

import com.jerocaller.libs.spoonsuits.web.jwt.DefaultJwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        UserDetails userOne = User.builder()
            .username("Kimquel")
            .password("kimquel1234")
            .roles("USER")
            .build();
        UserDetails userTwo = User.builder()
            .username("Jeongdb")
            .password("jeongdb1122")
            .roles("USER", "STAFF")
            .build();
        UserDetails userThree = User.builder()
            .username("Javas")
            .password("javas2233")
            .roles("USER", "STAFF", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(userOne, userTwo, userThree);
    }

}
