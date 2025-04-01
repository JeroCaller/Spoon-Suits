package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jayway.jsonpath.JsonPath;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.TokenNotFoundInCookieException;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.exception.classes.TokenNotValidException;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.util.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtProperties jwtProperties;

    private TokenUtil tokenUtil;
    private final String loginUri = "/test/auth";
    private final String tokenUri = "/test/tokens";

    @BeforeEach
    void initEach() {
        tokenUtil = new TokenUtil(mockMvc, loginUri);
    }

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
        유효한 리프레시 토큰으로 액세스 토큰 갱신 요청 시
        새로운 액세스 토큰을 생성하여 반환하는지 테스트.
    """)
    void getNewAccessTokenByValidRefreshTokenTest() throws Exception {

        // 테스트를 위해 미리 로그인
        MemberLogin memberLogin = MemberLogin.builder()
            .username("Jeongdb")
            .password("jeongdb1122")
            .build();
        UserDetails user = User.builder()
            .username(memberLogin.getUsername())
            .password(memberLogin.getPassword())
            .build();

        // 만료된 액세스 토큰 생성.
        String beforeAccessToken = jwtAuthenticationProvider.createToken(user, 0);
        assertThat(jwtAuthenticationProvider.validateToken(beforeAccessToken))
            .isFalse();

        // 로그인으로 리프레시 토큰 쿠키 추출.
        Cookie refreshTokenCookie = tokenUtil.getCookie(
            memberLogin,
            jwtProperties.getToken()
                .getRefresh()
                .getCookieName()
        );

        MvcResult newAccessTokenMvcResult = mockMvc.perform(
            get(tokenUri)
                .header(
                    JwtAuthenticationProvider.AUTHORIZATION,
                    JwtAuthenticationProvider.BEARER
                )
                .cookie(refreshTokenCookie)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").doesNotExist())
            .andDo(print())
            .andReturn();

        String newAccessToken = JsonPath.read(
            newAccessTokenMvcResult.getResponse().getContentAsString(),
            "$.data.accessToken"
        );
        assertThat(jwtAuthenticationProvider.validateToken(newAccessToken))
            .isTrue();
        assertThat(newAccessToken).isNotEqualTo(beforeAccessToken);

        Claims claims = jwtAuthenticationProvider.extractClaims(newAccessToken);
        assertThat(claims.getSubject()).isEqualTo(memberLogin.getUsername());
        assertThat(claims.getIssuer()).isEqualTo(jwtProperties.getIssuer());
        log.info("claims from new access token: {}", claims);

    }

    @Test
    @DisplayName("""
        리프레시 토큰이 담긴 쿠키가 없다면 서버 내부적으로
        TokenNotFoundInCookieException이 일어나면서 새 액세스 토큰 발급에
        실패한다.
    """)
    void noRefreshTokenInCookieWillFailtoGetNewAccessTokenTest() throws Exception {

        mockMvc.perform(get(tokenUri))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                .value(new TokenNotFoundInCookieException().getMessage())
            )
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());
    }

    @Test
    @DisplayName("""
        만료된 리프레시 토큰이 쿠키에 있는 경우, TokenNotValidException이 일어나면서
        새 액세스 토큰 발급에 실패한다.
    """)
    void expiredRefreshTokenInCookieWillFailToGetNewAccessTokenTest()
        throws Exception
    {

        // 만료된 리프레시 토큰 생성
        UserDetails mockUser = User.builder()
            .username("Javas")
            .password("javas1122")
            .build();
        String expiredRefreshToken = jwtAuthenticationProvider
            .createToken(mockUser, 0);
        assertThat(jwtAuthenticationProvider.validateToken(expiredRefreshToken))
            .isFalse();

        Cookie expiredRefreshTokenCookie = new Cookie(
            jwtProperties.getToken()
                .getRefresh()
                .getCookieName(),
            expiredRefreshToken
        );

        // 만료된 리프레시 토큰 쿠키로 새 액세스 토큰 발급 시도
        mockMvc.perform(
            get(tokenUri)
                .cookie(expiredRefreshTokenCookie)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message")
                .value(new TokenNotValidException().getMessage())
            )
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());

    }

}
