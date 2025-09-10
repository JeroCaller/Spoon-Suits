package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.selenium.test;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config.SeleniumOptionConfigUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.CookieService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 *     참고사항)
 * </p>
 * <p>
 *     셀레니움을 이용한 테스트는 실제 웹 브라우저를 띄워 테스트를 하는 방식이다.
 *     이 때 테스트 대상 URL이 실제로 서비스하는 상태여야 한다.
 *     이 테스트 클래스에서는 src/main/java에 정의된 spring context를 가져온 후, 테스트용 앱을
 *     실행시켜 여기에 HTTP 요청 및 응답 내용을 토대로 테스트한다.
 *     여기서는 혹시라도 같은 포트를 사용하는 다른 프로그램과의 충돌을 회피하기 위해
 *     RANDOM_PORT 상수를 사용하여 테스트 앱에 포트 번호를 에러 없이 부여하도록 한다.
 *     이 방식의 테스트에서는 별도로 src/main/java에 있는 앱을 실행시킬 필요가 전혀 없다.
 * </p>
 *
 * <p>
 *     크롬 브라우저를 기준으로 테스트한다.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class CookieControllerOnBrowserTest {

    private WebDriver webDriver;

    @LocalServerPort
    private int port;  // RANDOM_PORT에 의해 랜덤으로 부여된 포트 번호

    @Autowired
    private CookieService cookieService;

    private String requestDomainUrl;
    private String requestMockCookiesCreationUrl;
    private String requestDeleteCookiesUrl;

    private final List<String> expectedMockCookieNames = Arrays.asList(
        "TEST-COOKIE-1",
        "TEST-COOKIE-2",
        "TEST-COOKIE-3"
    );

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver(SeleniumOptionConfigUtils.getChromeOption());
        requestDomainUrl = String.format("http://127.0.0.1:%d/test/cookie", port);
        requestMockCookiesCreationUrl = UriComponentsBuilder
            .fromUriString(requestDomainUrl)
            .path("/many")
            .build()
            .toUriString();
        requestDeleteCookiesUrl = UriComponentsBuilder
            .fromUriString(requestDomainUrl)
            .path("/selenium/delete")
            .build()
            .toUriString();

        log.info("테스트용 HTTP 요청 URL: {}", requestDomainUrl);
        log.info("테스트용 다중 쿠키 생성 URL: {}", requestMockCookiesCreationUrl);
        log.info("쿠키 삭제 URL: {}", requestDeleteCookiesUrl);
    }

    @AfterEach
    void clean() {
        webDriver.quit();
    }

    @Test
    @DisplayName("이 테스트 클래스의 작동 여부 확인")
    void initTest() {
        log.info("{}에서 테스트 실행", this.getClass().getName());
        log.info("현재 포트 번호: {}", port);
    }

    @Test
    @DisplayName("CookieService load 여부 테스트")
    void cookieServiceLoadTest() {
        assertThat(cookieService.getCookieRequestsForTest().size()).isNotZero();
    }

    @Test
    @DisplayName("""
        API 호출 시 쿠키 하나가 생성되는지 확인.
        Selenium을 이용한 테스트가 문제 없이 진행되는지도 확인
    """)
    void createDefaultCookieTest() {

        webDriver.get(requestDomainUrl);

        assertThat(webDriver.manage().getCookies().size()).isNotZero();

        Cookie targetCookie = webDriver.manage().getCookieNamed("Good");
        assertThat(targetCookie).isNotNull();
        assertThat(targetCookie.getValue()).isEqualTo("wow");
        assertThat(targetCookie.getPath()).isEqualTo("/");
        assertThat(targetCookie.isHttpOnly()).isTrue();
        assertThat(targetCookie.isSecure()).isFalse();

    }

    @Test
    @DisplayName("여러 개의 테스트용 쿠키 생성 여부 확인 테스트")
    void doesSeveralCookiesCreatedTest() {

        webDriver.get(requestMockCookiesCreationUrl);

        assertThat(requestMockCookiesCreationUrl)
            .isEqualTo(webDriver.getCurrentUrl());
        log.info("실제 브라우저 URL: {}", webDriver.getCurrentUrl());
        log.info("브라우저 탭 제목: {}", webDriver.getTitle());

        assertThat(webDriver.manage().getCookieNamed("TEST-COOKIE-1"))
            .isNotNull();
        Set<Cookie> actualCookies = webDriver.manage().getCookies();

        int sameCount = howManySameNamesAreContainedInCookie(
            expectedMockCookieNames,
            actualCookies
        );
        log.info("{} is null?", webDriver.manage()
            .getCookieNamed("TEST-COOKIE-1"));
        assertThat(actualCookies.size()).isEqualTo(3);
        assertThat(sameCount).isEqualTo(3);

    }

    @Test
    @DisplayName("쿠키 하나 생성 후 해당 쿠키 삭제 API 호출 시 삭제 여부 확인")
    void deleteDefaultCookieTest() {

        final String targetCookieName = "Good";

        // API 호출을 통해 테스트용 쿠키 생성.
        webDriver.get(requestDomainUrl);
        Cookie givenCookie = webDriver.manage().getCookieNamed(targetCookieName);
        assertThat(givenCookie).isNotNull();
        assertThat(webDriver.manage().getCookies().size()).isOne();

        // 쿠키 삭제 API의 URI 생성
        final String paramNameForCookie = "name";
        final String deleteCookieUrl = UriComponentsBuilder
            .fromUriString(requestDeleteCookiesUrl)
            .queryParam(paramNameForCookie,targetCookieName)
            .build()
            .toUriString();
        log.info("쿠키 삭제 API 요청을 위해 생성된 URI: {}", deleteCookieUrl);

        webDriver.get(deleteCookieUrl);

        Cookie actualCookie = webDriver.manage()
            .getCookieNamed(targetCookieName);
        
        assertThat(actualCookie).isNull();
        assertThat(webDriver.manage().getCookies().size()).isZero();

    }

    @Test
    @DisplayName("""
        3개의 테스트용 쿠키 생성 후 쿠키 삭제 API 호출 시
        2개만 삭제 가능한지 확인.
    """)
    void deleteOnlySomeCookies() {

        // 테스트용 쿠키 여러 개 생성.
        webDriver.get(requestMockCookiesCreationUrl);

        // 쿠키 삭제를 위한 엔드포인트 URI 생성
        final List<String> targetCookieNames = expectedMockCookieNames
            .subList(0, 2);
        final String deleteCookieUrl = UriComponentsBuilder
            .fromUriString(requestDeleteCookiesUrl)
            .queryParams(getUriWithQueryForDeleteCookie(
                "name",
                targetCookieNames
            ))
            .build()
            .toUriString();
        log.info("테스트용 쿠키 삭제 URI: {}", deleteCookieUrl);

        // 쿠키 삭제 API 호출 및 결과 확인.
        webDriver.get(deleteCookieUrl);
        Set<Cookie> resultCookies = webDriver.manage().getCookies();
        assertThat(resultCookies.size()).isOne();
        int expectedSameCount = howManySameNamesAreContainedInCookie(
            Collections.singletonList(expectedMockCookieNames.getLast()),
            resultCookies
        );
        assertThat(expectedSameCount).isOne();
        assertThat(webDriver.manage().getCookieNamed(
            expectedMockCookieNames.getLast()
        )).isNotNull();

    }

    @Test
    @DisplayName("""
        3개의 테스트용 쿠키들이 브라우저에 저장된 상태에서
        존재하지 않는 쿠키 삭제 시도 시 이미 저장된 쿠키들은 그대로 있는지 테스트.
    """)
    void dontDeleteInnocentCookies() {

        // 테스트용 쿠키 여러 개 생성.
        webDriver.get(requestMockCookiesCreationUrl);

        final String targetCookieName = "NO-COOKIE";
        final String deleteCookieUri = UriComponentsBuilder
            .fromUriString(requestDeleteCookiesUrl)
            .queryParam("name", targetCookieName)
            .build()
            .toUriString();

        // 대상 쿠키 삭제
        webDriver.get(deleteCookieUri);
        Set<Cookie> actualCookies = webDriver.manage().getCookies();
        int sameCount = howManySameNamesAreContainedInCookie(
            expectedMockCookieNames,
            actualCookies
        );
        assertThat(actualCookies.size()).isEqualTo(3);
        assertThat(sameCount).isEqualTo(3);

    }

    /**
     * 쿠키 삭제 API 호출용 URI의 Query String으로 삭제 대상 쿠키 이름들을 추가.
     *
     * @param commonParamName Query String에서 사용할 공통 name
     * @param cookieNames 삭제하고자 하는 쿠키 이름 리스트.
     * @return
     */
    private MultiValueMap<String, String> getUriWithQueryForDeleteCookie(
        String commonParamName,
        List<String> cookieNames
    ) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(commonParamName, cookieNames);
        return map;
    }

    /**
     * 웹 브라우저 상에서 예상되는 이름을 가진 쿠키들이 얼마나 있는지 그 개수를 반환.
     *
     * @param expectedCookieNames 존재할거라 예상되는 쿠키 이름 리스트.
     * @param actualCookies 실제 쿠키들의 Set
     * @return
     */
    private int howManySameNamesAreContainedInCookie(
        List<String> expectedCookieNames,
        Set<Cookie> actualCookies
    ) {

        int sameCount = 0;
        for (String name : expectedCookieNames) {
            for (Cookie actualCookie : actualCookies) {
                if (actualCookie.getName().equals(name)) {
                    ++sameCount;
                    break;
                }
            }
        }
        return sameCount;
    }

}
