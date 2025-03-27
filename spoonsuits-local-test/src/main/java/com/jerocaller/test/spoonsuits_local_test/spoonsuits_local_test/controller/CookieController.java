package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieRequest;
import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUser;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUserResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@RestController
@RequestMapping("/test/cookie")
@RequiredArgsConstructor
@Slf4j
public class CookieController {

    private final CookieUtils cookieUtils;
    private final CookieService cookieService;

    private final String USER_COOKIE_NAME = "USER";

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

    /**
     * 가짜 유저 신규 가입 API
     * 유저 정보 객체를 직렬화하여 쿠키의 값으로 대입.
     * 유저 정보가 담긴 쿠키가 생성됨.
     *
     * Selenium에선 GET 이외 다른 HTTP Method 사용 불가로 인해
     * 어쩔 수 없이 GET으로 설정함.
     *
     * @param response
     * @return
     */
    @GetMapping("/register/users/my")
    public ResponseEntity<Object> registerUser(
        HttpServletResponse response,
        @RequestParam("username") String username,
        @RequestParam("age") int age
    ) {

        SiteUser siteUser = SiteUser.builder()
            .username(username)
            .age(age)
            .build();
        String serializedUserInfo = cookieUtils.serialize(siteUser);

        CookieRequest cookieRequest = CookieRequest.builder()
            .cookieName(USER_COOKIE_NAME)
            .cookieValue(serializedUserInfo)
            .maxAge(60 * 5)
            .build();
        cookieUtils.addCookie(response, cookieRequest);
        return ResponseEntity.ok(siteUser);
    }

    /**
     * 테스트용 현재 유저 정보 반환 API
     * 요청에서 사용자 정보가 들어 있는 유저 정보를 추출, 역직렬화하여
     * 자바 객체로 변환 후, 해당 객체로부터 사용자 정보를 문자열로 추출하여 응답한다.
     * 이 API 호출 전 반드시 이미 "POST /users/my"를 호출하여 사용자 정보가 담긴
     * 쿠키를 보유한 상태여야 한다.
     *
     * @param request
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @GetMapping("/users/my")
    public ResponseEntity<Object> getMyUserInfo(HttpServletRequest request)
        throws IOException, ClassNotFoundException
    {

        Cookie userCookie = WebUtils.getCookie(request, USER_COOKIE_NAME);
        log.info(userCookie.toString());
        log.info(userCookie.getName());
        log.info(userCookie.getValue());

        SiteUser siteUser = cookieUtils.deserialize(userCookie, SiteUser.class);
        log.info(siteUser.toString());
        SiteUserResponse siteUserResponse = SiteUserResponse.builder()
            .siteUser(siteUser)
            .message(siteUser.getStringOfUserInfo())
            .build();
        return ResponseEntity.ok(siteUserResponse);
    }

}
