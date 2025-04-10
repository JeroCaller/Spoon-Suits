package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.libs.spoonsuits.web.jwt.impl.DefaultJwtAuthenticationProviderImpl;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member.MemberLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class JwtAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private DefaultJwtAuthenticationProviderImpl jwtProvider;

    @Test
    @DisplayName("테스트 클래스 실행 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트 클래스가 잘 실행되고 있다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("""
       POST /test/auth로 로그인 요청 성공 시 Access & Refresh Token을 응답하는지 테스트.
    """)
    void getTokensByLoginTest() throws Exception {

        final String uri = "/test/auth";
        MemberLogin mockMemberLogin = MemberLogin.builder()
            .username("Kimquel")
            .password("kimquel1234")
            .build();

        MvcResult mvcResult = mockMvc.perform(post(uri)
            .content(objectMapper.writeValueAsString(mockMemberLogin))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value("로그인 성공"))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username")
                .value(mockMemberLogin.getUsername())
            )
            .andExpect(jsonPath("$.data.password")
                .value(mockMemberLogin.getPassword())
            )
            .andExpect(jsonPath("$.data.tokenDto").exists())
            .andExpect(jsonPath("$.data.tokenDto.accessToken").exists())
            .andExpect(jsonPath("$.data.tokenDto.refreshToken").exists())
            .andExpect(cookie().exists(jwtProperties.getToken()
                .getRefresh().getCookieName())
            )
            .andDo(print())
            .andReturn();

        // JWT 토큰 여부 및 토큰 세부 정보 검증.
        // Access Token 검증.
        String accessToken = JsonPath.read(
            mvcResult.getResponse().getContentAsString(),
            "$.data.tokenDto.accessToken"
        );
        tokenTest(accessToken, mockMemberLogin);

        // Refresh Token 검증
        String refreshToken = JsonPath.read(
            mvcResult.getResponse().getContentAsString(),
            "$.data.tokenDto.refreshToken"
        );
        tokenTest(refreshToken, mockMemberLogin);

        assertThat(accessToken).isNotEqualTo(refreshToken);

    }

    private void tokenTest(String token, MemberLogin mockMemberLogin) {

        Claims claimsFromToken = jwtProvider.extractClaims(token);
        Map<String, Object> jwtHeader = getJwtHeader(token);
        List<String> roles = (List<String>) claimsFromToken.get("roles");

        log.info(claimsFromToken.keySet().toString());
        log.info(jwtHeader.toString());
        log.info(roles.toString());

        assertThat(claimsFromToken.getIssuer())
            .isEqualTo(jwtProperties.getIssuer());
        assertThat(jwtHeader.get("typ"))
            .isEqualTo("JWT");
        assertThat(claimsFromToken.getSubject())
            .isEqualTo(mockMemberLogin.getUsername());
        assertThat(roles.getFirst().startsWith("ROLE_")).isTrue();

    }

    private Map<String, Object> getJwtHeader(String token) {

        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey()
            .getBytes(StandardCharsets.UTF_8)
        );

        Jws<Claims> jws = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token);
        return jws.getHeader();
    }

}