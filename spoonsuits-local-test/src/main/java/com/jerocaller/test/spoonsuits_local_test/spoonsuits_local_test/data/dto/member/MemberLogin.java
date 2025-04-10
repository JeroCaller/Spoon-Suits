package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLogin {

    private String username;
    private String password;

}
