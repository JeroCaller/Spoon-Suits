package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.siteuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 직렬화, 역직렬화 테스트용 가짜 유저 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SiteUser implements Serializable {

    private String username;
    private int age;

    /**
     * 테스트용 메서드. 사용자의 정보를 문자열로 추출, 반환.
     *
     * @return
     */
    public  String getStringOfUserInfo() {

        return String.format(
            "유저 정보) username: %s, 나이: %d",
            getUsername(),
            getAge()
        );
    }

}
