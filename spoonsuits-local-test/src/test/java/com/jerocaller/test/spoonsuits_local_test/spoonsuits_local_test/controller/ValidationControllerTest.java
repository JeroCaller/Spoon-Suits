package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.SiteUserRegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class})
@Slf4j
class ValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String defaultRequestUri = "/test/users";

    @Test
    @DisplayName("UserControllerTest 초기화 테스트")
    void initTest() {
        log.info("=== {} 클래스에서 테스트 시작. ===", this.getClass().getName());
    }

    @Test
    @DisplayName("""
        유효성 검사 모두 성공 시 응답 JSON 내부에는 유효성 검사 관련 메시지가 포함되지 않는다.
    """)
    void noValidationFailedMessageContainedInJsonTest() throws Exception {

        SiteUserRegisterDto mockUser = SiteUserRegisterDto.builder()
            .username("kimquel")
            .age(23)
            .email("kimquel@kimquel.com")
            .phone("010-1234-5678")
            .build();

        mockMvc.perform(post(defaultRequestUri)
                .content(objectMapper.writeValueAsString(mockUser))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.httpStatus").exists())
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message").value("회원 가입 성공"))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());

    }

    @Test
    @DisplayName("유저 이름 미기입 시 유효성 검사 실패에 따른 응답 JSON 생성 여부 테스트")
    void isUsernameContainedInValidationFailedMessageTest() throws Exception {

        SiteUserRegisterDto mockUser = SiteUserRegisterDto.builder()
            .username(null)
            .age(23)
            .email("kimquel@kimquel.com")
            .phone("010-1234-5678")
            .build();

        mockMvc.perform(post(defaultRequestUri)
            .content(objectMapper.writeValueAsString(mockUser))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").exists())
            .andDo(print());

    }

    @Test
    @DisplayName("전화번호의 유효성 검사 실패 시 응답 JSON에도 반영되는지 테스트")
    void isPhoneValidationFailedContainedInJsonTest() throws Exception {

        SiteUserRegisterDto mockUser = SiteUserRegisterDto.builder()
            .username("kimquel")
            .age(23)
            .email("kimquel@kimquel.com")
            .phone("01-12-34")
            .build();

        mockMvc.perform(post(defaultRequestUri)
                .content(objectMapper.writeValueAsString(mockUser))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.phone").exists())
            .andDo(print());

    }

    @Test
    @DisplayName("""
        두 개의 필드에서 유효성 검사 실패 시 둘 모두 실패 메시지에 반영되는지 테스트
    """)
    void areTwoValidationFailedMessagesContainedInJsonTest() throws Exception {

        SiteUserRegisterDto mockUser = SiteUserRegisterDto.builder()
            .username("kimquel")
            .age(18)
            .email("kimquelakimquel.com")
            .phone("010-1234-5678")
            .build();

        mockMvc.perform(post(defaultRequestUri)
                .content(objectMapper.writeValueAsString(mockUser))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").doesNotExist())
            .andExpect(jsonPath("$.data.age").exists())
            .andExpect(jsonPath("$.data.email").exists())
            .andExpect(jsonPath("$.data.phone").doesNotExist())
            .andDo(print());

    }

    @Test
    @DisplayName("모든 필드에서 유효성 검사 실패 시 응답 JSON에도 모두 반영되는지 테스트")
    void allValidationFailedMessageContainedInJsonTest() throws Exception {

        SiteUserRegisterDto mockUser = SiteUserRegisterDto.builder()
            .username(null)
            .age(89)
            .email("kimquel-kimquel.com")
            .phone("012-abc-5678")
            .build();

        mockMvc.perform(post(defaultRequestUri)
                .content(objectMapper.writeValueAsString(mockUser))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").exists())
            .andExpect(jsonPath("$.data.age").exists())
            .andExpect(jsonPath("$.data.email").exists())
            .andExpect(jsonPath("$.data.phone").exists())
            .andDo(print());

    }

}