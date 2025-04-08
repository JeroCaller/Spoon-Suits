package com.jerocaller.libs.spoonsuits.web.cookie;

import jakarta.servlet.http.Cookie;

/**
 * <p>
 *     CookieUtils에 사용될 쿠키 설정을 위한 인터페이스
 * </p>
 * <p>
 *     주의) 유연한 쿠키 사용을 위해 쿠키의 name, value, maxAge 설정은
 *     CookieRequest 사용을 권고함.
 * </p>
 * <p>
 *     기본적으로는 DefaultCookieConfigurerImpl이 이 인터페이스를 구현하고 있음.
 *     해당 구현체는 개발용으로 쿠키 설정이 되어 있음.
 *     만약 쿠키 설정 커스텀을 하고자 한다면
 * </p>
 * <ol>
 *     <li>CookieConfigurer를 implements하며, </li>
 *     <li>해당 구현체에 <code>@Primary</code> 및
 *         <code>@Component</code>어노테이션을 부여한다.
 *     </li>
 * </ol>
 *
 * <p>
 *     쿠키 설정 커스텀 예시)
 * </p>
 * <pre>{@code
 * @Primary
 * @Component
 * public class MyCookieConfigurer implements CookieConfigurer {
 *
 *     @Override
 *     public void configureCookie(Cookie cookie) {
 *         cookie.setHttpOnly(true);
 *         cookie.setPath("/");
 *         cookie.setSecure(True);
 *     }
 * }
 * }
 * </pre>
 *
 * @see CookieUtils
 * @see com.jerocaller.libs.spoonsuits.web.cookie.impl.DefaultCookieConfigurerImpl
 */
@FunctionalInterface
public interface CookieConfigurer {

    /**
     * 쿠키 설정 콜백 메서드.
     *
     * @param cookie CookieUtils 내부 메서드에서 사용될 설정 대상 쿠키
     */
    void configureCookie(Cookie cookie);

}
