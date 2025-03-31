package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("테스트 클래스 실행 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트 클래스가 잘 실행되고 있다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    // TODO : 테스트 코드 계속 작성.
    @Test
    @DisplayName("""
       POST /test/auth로 로그인 요청 성공 시 Access & Refresh Token을 응답하는지 테스트.
    """)
    void getTokensByLoginTest() throws Exception {

        final String uri = "/test/auth";
        MemberLogin mockMemberLogin = MemberLogin.builder()
            .username("kimquel")
            .password("kimquel1234")
            .build();

        mockMvc.perform(post(uri)
            .content(objectMapper.writeValueAsString(mockMemberLogin))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value("로그인 성공"))
            .andExpect(jsonPath("$.data").exists())
            .andDo(print());

    }

}