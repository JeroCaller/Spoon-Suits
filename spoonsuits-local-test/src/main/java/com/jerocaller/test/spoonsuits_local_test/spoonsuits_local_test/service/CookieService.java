package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CookieService {

    /**
     * 테스트용 쿠키 데이터 생성.
     *
     * @return
     */
    public List<CookieRequest> getCookieRequestsForTest() {
        return Arrays.asList(
            CookieRequest.builder()
                .cookieName("TEST-COOKIE-1")
                .cookieValue("First-cookie-for-test")
                .maxAge(60)
                .build(),
            CookieRequest.builder()
                .cookieName("TEST-COOKIE-2")
                .cookieValue("Second-cookie-for-test")
                .maxAge(60 * 2)
                .build(),
            CookieRequest.builder()
                .cookieName("TEST-COOKIE-3")
                .cookieValue("Third-cookie-for-test")
                .maxAge(60 * 3)
                .build()
        );
    }

}
