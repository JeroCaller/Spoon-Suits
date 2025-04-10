package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.cookie;

import com.jerocaller.libs.spoonsuits.web.cookie.CookieUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config.TestCookieConfigurer;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config.TestSecurityDisableConfig;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.CookieController;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.CookieService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CookieController.class})
@Import({CookieUtils.class, TestCookieConfigurer.class, CookieService.class})
@ContextConfiguration(classes = {TestSecurityDisableConfig.class})
@Slf4j
class CookieControllerWithCustomCookieConfigurerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("테스트 클래스 작동 여부 확인")
    void initTest() {
        log.info(" === {} 에서 테스트 === ", this.getClass().getName());
    }

    @Test
    @DisplayName("""
        쿠키 생성 API를 호출한 후, 테스트용 커스텀 CookieConfigurer가 적용되었는지 확인.
    """)
    void CustomCookieConfigurerTest() throws Exception {

        final String uri = "/test/cookie";
        final String cookieName = "Good";

        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(cookie().exists(cookieName))
            .andExpect(cookie().maxAge(cookieName, 60 * 5))
            .andExpect(cookie().secure(cookieName, false))
            .andExpect(cookie().httpOnly(cookieName, false))
            .andExpect(cookie().path(cookieName, "/cookie/test"))
            .andExpect(cookie().attribute(
                cookieName,
                "TEST_CONFIGURE",
                "This is for Test"
            ))
            .andDo(print());

    }

}
