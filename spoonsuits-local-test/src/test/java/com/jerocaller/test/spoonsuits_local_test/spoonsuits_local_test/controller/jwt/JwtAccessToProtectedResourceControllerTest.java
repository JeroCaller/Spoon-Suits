package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member.MemberLogin;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
@Slf4j
public class JwtAccessToProtectedResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TokenUtil tokenUtil;

    private final String loginUri = "/test/auth";
    private final String resourceUri = "/test/books";
    private final String resourceWithMyInfo = "/test/books/with-my-info";

    @AfterEach
    void cleanUp() {
        tokenUtil = null;
    }

    @Test
    @DisplayName("테스트 클래스 실행 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트 클래스가 잘 실행되고 있다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("""
        Book 조회 API 호출을 통해 JWT 인증 필터가 제대로 동작하여 인증이 되는지 테스트.
    """)
    void jwtAuthenticationTest() throws Exception {

        MemberLogin mockMemberLogin = MemberLogin.builder()
            .username("Kimquel")
            .password("kimquel1234")
            .build();

        MvcResult loginResult = mockMvc.perform(post(loginUri)
            .content(objectMapper.writeValueAsString(mockMemberLogin))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.tokenDto.accessToken").exists())
            .andReturn();

        String accessToken = JsonPath.read(
            loginResult.getResponse().getContentAsString(),
            "$.data.tokenDto.accessToken"
        );

        mockMvc.perform(get(resourceUri)
            .header("Authorization", "Bearer " + accessToken)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data[0].title")
                .value("자바 타파하기")
            )
            .andDo(print());

    }

    @Test
    @DisplayName("""
        미인증된 유저는 Book을 조회할 수 없다.
    """)
    void NotAuthenticatedUserCannotApproachBook() throws Exception {

        mockMvc.perform(get(resourceUri))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$").doesNotExist())
            .andDo(print());

    }

    @Test
    @DisplayName("""
        인증된 상태로 모든 서적 요청 시 모든 서적과 더불어
        현재 인증된 사용자의 정보도 가져오는지 테스트.
    """)
    void getAllBooksWithCurrentUserInfo() throws Exception {

        MemberLogin admin = MemberLogin.builder()
            .username("Javas")
            .password("javas2233")
            .build();

        tokenUtil = new TokenUtil(mockMvc, loginUri);
        String accessToken = tokenUtil.getAccessToken(admin);

        mockMvc.perform(get(resourceWithMyInfo)
                .header("Authorization", "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.books").exists())
            .andExpect(jsonPath("$.data.user").exists())
            .andExpect(jsonPath("$.data.books[0].title")
                .value("자바 타파하기"))
            .andExpect(jsonPath("$.data.user.username")
                .value("Javas"))
            .andExpect(jsonPath("$.data.user.roles[0].authority")
                .value("ROLE_ADMIN"))
            .andExpect(jsonPath("$.data.user.tokenDto").doesNotExist())
            .andDo(print());

    }

}
