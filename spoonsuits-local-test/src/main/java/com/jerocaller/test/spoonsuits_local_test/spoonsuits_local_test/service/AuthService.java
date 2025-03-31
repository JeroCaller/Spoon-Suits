package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberWithTokenResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.TokenDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Member;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.PasswordNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public MemberWithTokenResponse login(MemberLogin memberLogin) {

        Member member = (Member) userDetailsService
            .loadUserByUsername(memberLogin.getUsername());

        if (!member.getPassword().equals(memberLogin.getPassword())) {
            throw new PasswordNotMatchException();
        }

        TokenDto tokenDto = tokenService.getNewTokens(member);

        return MemberWithTokenResponse.toDto(member, tokenDto);
    }

}
