package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.MemberLogin;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * <p>
 *     로그인 후 토큰을 얻기 위한 유틸 클래스.
 * </p>
 * <p>
 *     정확한 테스트를 위해 <code>@BeforeEach</code> 등을 활용하여
 *     매 테스트마다 MockMvc 객체를 주입하도록 고안함.
 * </p>
 */
public class TokenUtil {

    private MockMvc mockMvc;
    private String loginUri;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TokenUtil() {}

    public TokenUtil(MockMvc mockMvc, String loginUri) {
        this.mockMvc = mockMvc;
        this.loginUri = loginUri;
    }

    public String getAccessToken(MemberLogin memberLogin) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(loginUri)
            .content(objectMapper.writeValueAsString(memberLogin))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn();

        return JsonPath.read(
            mvcResult.getResponse().getContentAsString(),
            "$.data.tokenDto.accessToken"
        );
    }

    public Cookie getCookie(
        MemberLogin memberLogin,
        String refreshTokenCookieName
    ) throws Exception {

        MvcResult mvcResult = mockMvc.perform(
            post(loginUri)
                .content(objectMapper.writeValueAsString(memberLogin))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();

        return mvcResult.getResponse().getCookie(refreshTokenCookieName);
    }

}
