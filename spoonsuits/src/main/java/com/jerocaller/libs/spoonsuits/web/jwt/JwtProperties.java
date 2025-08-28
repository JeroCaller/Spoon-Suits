package com.jerocaller.libs.spoonsuits.web.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * <p>
 *     application.properties(또는 .yml) 파일로부터
 *     JWT 관련 설정값들을 가져오는 Spring bean.
 * </p>
 *
 * <p>application.yml 설정 예시</p>
 * <pre><code>jwt:
 * issuer: example@gmail.com
 * secret-key: your-secret-key
 * token:
 *   access:
 *     expiry: PT24H // (1일)
 *     cookie-name: ACCESS-TOKEN
 *   refresh:
 *     expiry: PT168H // (7일)
 *     cookie-name: REFRESH-TOKEN
 * </code></pre>
 *
 * <p>기본값)</p>
 * <ul>
 *     <li>token.access.expiry: 1일</li>
 *     <li>token.refresh.expiry: 7일</li>
 *     <li>token.access.cookie-name: ACCESS-TOKEN</li>
 *     <li>token.refresh.cookie-name: REFRESH-TOKEN</li>
 * </ul>
 *
 * <p>Note)</p>
 * <ul>
 *     <li>
 *         DefaultJwtAuthenticationProviderImpl에서 의존성 주입받아
 *         사용할 클래스.
 *     </li>
 *     <li>
 *         생성자 바인딩 방식으로 구현하여 설정값들의 불변성 유지 가능.
 *     </li>
 *     <li>
 *         application.yml의 expiry 값 형태는
 *         <a href="https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#toString--">
 *             Duration.toString
 *         </a>문서를 참고.
 *     </li>
 * </ul>
 *
 *
 * @see
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#toString--">
 *     Duration.toString
 * </a>
 */
@Getter
@ToString
@ConfigurationProperties(prefix = "jwt")
@AllArgsConstructor
public class JwtProperties {

    private final String issuer;
    private final String secretKey;
    private final Token token;

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Token {

        private final Access access;
        private final Refresh refresh;

        @Getter
        @ToString
        public static class Access {

            private final Duration expiry;
            private final String cookieName;

            public Access(
                @DefaultValue("PT24H") Duration expiry,
                @DefaultValue("ACCESS-TOKEN") String cookieName
            ) {
                this.expiry = expiry;
                this.cookieName = cookieName;
            }
        }

        @Getter
        @ToString
        public static class Refresh {

            private final Duration expiry;
            private final String cookieName;

            public Refresh(
                @DefaultValue("PT168H") Duration expiry,
                @DefaultValue("REFRESH-TOKEN") String cookieName
            ) {
                this.expiry = expiry;
                this.cookieName = cookieName;
            }
        }
    }
}
