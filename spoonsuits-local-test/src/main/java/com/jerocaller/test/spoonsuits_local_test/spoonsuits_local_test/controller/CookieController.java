package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieRequest;
import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/cookie")
@RequiredArgsConstructor
public class CookieController {

    private final CookieUtils cookieUtils;
    private final CookieService cookieService;

    @GetMapping
    public ResponseEntity<Object> createCookie(HttpServletResponse response) {

        CookieRequest cookieRequest = CookieRequest.builder()
            .cookieName("Good")
            .cookieValue("wow")
            .maxAge(60 * 60)
            .build();
        cookieUtils.addCookie(response, cookieRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/custom")
    public ResponseEntity<Object> createCustomCookie(
        HttpServletResponse response,
        @RequestBody CookieRequest cookieRequest
    ) {

        cookieUtils.addCookie(response, cookieRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCookies(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam("name") String... cookieNames
    ) {

        cookieUtils.deleteCookies(request, response, cookieNames);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/many")
    public ResponseEntity<Object> createSeveralCookies(
        HttpServletResponse response
    ) {

        cookieService.getCookieRequestsForTest().forEach(cookieRequest -> {
            cookieUtils.addCookie(response, cookieRequest);
        });

        return ResponseEntity.ok().build();
    }

    /**
     * Selenium 기반 테스트에서는 get 요청만 가능하므로 이를 위한 쿠키 삭제 API 별도 제작.
     *
     * @param request
     * @param response
     * @param cookieNames
     * @return
     */
    @GetMapping("/selenium/delete")
    public ResponseEntity<Object> deleteCookiesForSeleniumTest(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam("name") String... cookieNames
    ) {

        cookieUtils.deleteCookies(request, response, cookieNames);

        return ResponseEntity.ok().build();
    }

}
