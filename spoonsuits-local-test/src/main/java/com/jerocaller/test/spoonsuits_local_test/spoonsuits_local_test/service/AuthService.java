package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member.MemberLogin;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member.MemberWithTokenResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.TokenDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.PasswordNotMatchException;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public MemberWithTokenResponse login(MemberLogin memberLogin) {

        UserDetails member = userDetailsService
            .loadUserByUsername(memberLogin.getUsername());

        if (!member.getPassword().equals(memberLogin.getPassword())) {
            throw new PasswordNotMatchException();
        }

        TokenDto tokenDto = tokenService.getNewTokens(member);

        return MemberWithTokenResponse.toDto(member, tokenDto);
    }

    /**
     * <p>
     *     현재 인증된 사용자 정보를 반환.
     * </p>
     * <p>
     *     내부적으로 <code>SecurityContextHolder.getContext().getAuthentication()</code>
     *     를 사용함.
     * </p>
     *
     * @return 토큰 없이 사용자 정보만 반환됨. 토큰 정보는 모두 null임.
     */
    public MemberWithTokenResponse getCurrentAuthInfo() {

        Authentication auth = SecurityContextHolder.getContext()
            .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UserNotAuthenticatedException();
        }

        log.info("Authentication#getPrincipal: {}", auth.getPrincipal());
        log.info("Authentication#getDetails: {}", auth.getDetails());
        log.info("Authentication#getAuthorities: {}", auth.getAuthorities());
        log.info("Authentication#getCredentials: {}", auth.getCredentials());

        UserDetails currentUser = User.builder()
            .username((String) auth.getPrincipal())
            .password("NO-PASSWORD")
            .authorities(auth.getAuthorities())
            .build();
        return MemberWithTokenResponse.toDto(currentUser, null);
    }

}
