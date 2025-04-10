package com.jerocaller.libs.spoonsuits.web.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
 *         기존에 알려진 생성자 바인딩 방식이 적용되지 않아
 *         setter 기반 바인딩으로 구현하였음.
 *         설정값들의 불변성을 유지해야하는 작업에서는 주의 요망.
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
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;
    private String secretKey;
    private Token token;

    @Getter
    @Setter
    @ToString
    public static class Token {

        private Access access;
        private Refresh refresh;

        @Getter
        @Setter
        @ToString
        public static class Access {

            private Duration expiry = Duration.ofDays(1);
            private String cookieName = "ACCESS-TOKEN";

        }

        @Getter
        @Setter
        @ToString
        public static class Refresh {

            private Duration expiry = Duration.ofDays(7);
            private String cookieName = "REFRESH-TOKEN";

        }

    }

}
