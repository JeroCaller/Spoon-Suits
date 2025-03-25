package com.jerocaller.libs.spoonsuits.web.cookie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookieRequest {

    private String cookieName;
    private String cookieValue;
    private int maxAge;

}
