package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * REST API 응답용 유저 응답 객체
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SiteUserResponse {

    private SiteUser siteUser;
    private String message;

}
