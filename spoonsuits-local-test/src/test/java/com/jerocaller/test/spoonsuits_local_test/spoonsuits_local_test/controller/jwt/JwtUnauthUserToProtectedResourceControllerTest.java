package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.libs.spoonsuits.web.jwt.impl.DefaultJwtAuthenticationProviderImpl;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 *     테스트 대상 컨트롤러: BookController
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.token.access.expiry=PT0M",
    "jwt.token.access.cookie-name=test-access-token"
})
@Slf4j
public class JwtUnauthUserToProtectedResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DefaultJwtAuthenticationProviderImpl jwtProvider;

    private final String allBookRequestUri = "/test/books";
    private final String loginUri = "/test/auth";

    @Test
    @DisplayName("테스트 클래스 실행 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트 클래스가 잘 실행되고 있다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("설정값이 테스트 클래스 내 정해진 대로 바뀌었는지 테스트")
    void propertyTest() {

        assertThat(jwtProperties.getToken().getAccess().getCookieName())
            .isEqualTo("test-access-token");
        assertThat(jwtProperties.getToken().getAccess().getExpiry().toMinutes())
            .isZero();

        // 손 대지 않은 다른 설정값들은 그대로인지 확인
        assertThat(jwtProperties.getIssuer()).isEqualTo("kimquel@good");
        assertThat(jwtProperties.getSecretKey())
            .isEqualTo("this-is-for-jwt-secret-key-which-has-very-long-length");
        assertThat(jwtProperties.getToken().getRefresh().getExpiry().toMinutes())
            .isEqualTo(10);
        assertThat(jwtProperties.getToken().getRefresh().getCookieName())
            .isEqualTo("refresh-token");

        log.info(jwtProperties.toString());

    }

    @Test
    @DisplayName("""
        만료된 Access Token으로 보호된 자원 요청 시
        서버는 이를 거절해야 한다.
    """)
    void serverShouldRejectRequestFromUserWithExpiredAccessToken()
        throws Exception
    {

        // 로그인 진행
        MemberLogin mockMemberLogin = MemberLogin.builder()
            .username("jeongdb")
            .password("jeongdb1122")
            .build();

        // 로그인은 성공해야 한다.
        MvcResult loginResult = mockMvc.perform(post(loginUri)
                .content(objectMapper.writeValueAsString(mockMemberLogin))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.tokenDto.accessToken").exists())
            .andDo(print())
            .andReturn();

        log.info("=== 로그인 절차 완료 ===");

        // 보호된 자원에 접근하기 위한 Access Token 추출
        String accessToken = JsonPath.read(
            loginResult.getResponse().getContentAsString(),
            "$.data.tokenDto.accessToken"
        );
        log.info("추출한 액세스 토큰: {}", accessToken);
        assertThat(jwtProvider.validateToken(accessToken)).isFalse();

        mockMvc.perform(get(allBookRequestUri)
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist())
            .andDo(print());

    }

}
