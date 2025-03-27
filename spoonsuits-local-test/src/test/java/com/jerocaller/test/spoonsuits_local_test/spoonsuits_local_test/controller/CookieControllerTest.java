package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.libs.spoonsuits.web.cookie.CookieRequest;
import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.libs.spoonsuits.web.cookie.impl.DefaultCookieConfigurerImpl;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUser;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.CookieService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CookieController.class)
@Import({CookieUtils.class, DefaultCookieConfigurerImpl.class, CookieService.class})
@Slf4j
class CookieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CookieUtils cookieUtils;

    @Test
    @DisplayName("이 테스트 클래스의 작동 자체에 문제 없는지 테스트.")
    void initTest() {
        log.info("=== {} 에서 테스트 실행 ===", this.getClass().getName());
    }

    @Test
    @DisplayName("DefaultCookieConfigurerImpl 적용 여부 테스트")
    void defaultCookieConfigurationTest() throws Exception {

        final String uri = "/test/cookie";
        final String cookieName = "Good";

        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly(cookieName, true))
            .andExpect(cookie().secure(cookieName, false))
            .andExpect(cookie().path(cookieName, "/"))
            .andDo(print());

    }

    @Test
    @DisplayName("의도된 쿠키가 생성되는지 테스트")
    void createCookieTest() throws Exception {

        final String uri = "/test/cookie";
        final String cookieName = "Good";

        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(cookie().exists(cookieName))
            .andExpect(cookie().value(cookieName, "wow"))
            .andExpect(cookie().maxAge(cookieName, 60 * 60))
            .andDo(print());

    }

    @Test
    @DisplayName("""
        JSON 형태로 쿠키 데이터 생성 후 컨트롤러에서 이 쿠키를 실제로 생성하는지 테스트
    """)
    void createCustomCookieTest() throws Exception {

        final String uri = "/test/cookie/custom";
        CookieRequest mockCookie = CookieRequest.builder()
            .cookieName("MOCK_COOKIE")
            .cookieValue("Cookie is delicious")
            .maxAge(60 * 60)
            .build();

        mockMvc.perform(post(uri)
            .content(objectMapper.writeValueAsString(mockCookie))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(mockCookie.getCookieName()))
            .andExpect(cookie().value(
                mockCookie.getCookieName(),
                mockCookie.getCookieValue())
            )
            .andExpect(cookie().maxAge(
                mockCookie.getCookieName(),
                mockCookie.getMaxAge())
            )
            .andDo(print());

    }

    @Test
    @DisplayName("/test/cookie/many : 테스트용 다중 쿠키 생성 테스트")
    void mockCookiesCreationTest() throws Exception {

        final String uri = "/test/cookie/many";

        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("TEST-COOKIE-1"))
            .andExpect(cookie().exists("TEST-COOKIE-2"))
            .andExpect(cookie().exists("TEST-COOKIE-3"))
            .andDo(print());

    }

    @Test
    @DisplayName("GET /test/register/cookie/users/my: 쿠키 직렬화 테스트")
    void registerUserTest() throws Exception {

        final String cookieName = "USER";
        final String uri = UriComponentsBuilder
            .fromUriString("/test/cookie/register/users/my")
            .queryParam("username", "jeongdb")
            .queryParam("age", 23)
            .build()
            .toUriString();

        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(cookie().exists(cookieName))
            .andExpect(cookie().maxAge(cookieName, 60 * 5))
            .andExpect(jsonPath("$.username")
                .value("jeongdb"))
            .andExpect(jsonPath("$.age").value(23))
            .andDo(print());
    }

    @Test
    @DisplayName("GET /test/cookie/users/my: 쿠키 역직렬화 테스트")
    void getMyUserInfoTest() throws Exception {

        final String uri = "/test/cookie/users/my";

        SiteUser mockUser = SiteUser.builder()
            .username("javas")
            .age(30)
            .build();
        Cookie mockCookie = new Cookie(
            "USER",
            cookieUtils.serialize(mockUser)
        );

        mockMvc.perform(get(uri).cookie(mockCookie))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.siteUser.username")
                .value(mockUser.getUsername())
            )
            .andExpect(jsonPath("$.siteUser.age")
                .value(mockUser.getAge())
            )
            .andDo(print());

    }

}