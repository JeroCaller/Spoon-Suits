package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberWithTokenResponse {

    private String username;
    private String password;
    private List<String> roles;
    private TokenDto tokenDto;

    public static MemberWithTokenResponse toDto(
        Member member,
        TokenDto tokenDto
    ) {
        return MemberWithTokenResponse.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .roles(member.getRoles())
            .tokenDto(tokenDto)
            .build();
    }

}
