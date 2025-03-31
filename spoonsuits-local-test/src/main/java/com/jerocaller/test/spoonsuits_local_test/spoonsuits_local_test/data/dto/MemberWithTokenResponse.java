package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberWithTokenResponse {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private TokenDto tokenDto;

    public static MemberWithTokenResponse toDto(
        UserDetails member,
        TokenDto tokenDto
    ) {
        return MemberWithTokenResponse.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .roles(member.getAuthorities())
            .tokenDto(tokenDto)
            .build();
    }

}
