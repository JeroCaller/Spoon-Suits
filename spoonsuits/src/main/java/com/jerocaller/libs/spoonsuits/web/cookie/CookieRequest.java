package com.jerocaller.libs.spoonsuits.web.cookie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>쿠키 추가를 위한 DTO 클래스.</p>
 * <p>
 *     쿠키 name과 value, 그리고 초 단위의 만료 기간을 설정하여 요청한다.
 * </p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookieRequest {

    private String cookieName;
    private String cookieValue;
    private int maxAge;

}
