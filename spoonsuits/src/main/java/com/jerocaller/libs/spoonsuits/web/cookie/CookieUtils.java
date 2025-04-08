package com.jerocaller.libs.spoonsuits.web.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * <p>쿠키 유틸리티 Spring Bean</p>
 *
 * <p>
 * 사용자의 쿠키 설정 커스텀을 가능하게 하기 위해 별도의 CookieConfigurer를 의존성 주입함.
 * 기본 CookieConfigurer는 DefaultCookieConfigurerImpl로, 개발용으로 설정함.
 * </p>
 *
 * <p>
 * 만약 쿠키 설정 커스텀을 하고자 한다면
 * <ol>
 *     <li>CookieConfigurer를 implements하며, </li>
 *     <li>해당 구현체에 <code>@Primary</code> 및
 *          <code>@Component</code>어노테이션을 부여한다.
 *     </li>
 * </ol>
 * </p>
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
 * <p>
 *     주의) CookieConfigurer를 이용한 쿠키 설정은 한 번 설정하면 모든 쿠키에 적용된다.
 *     따라서 만약 각 쿠키마다의 쿠키 설정을 달리해야한다면 이 유틸리티 사용은 부적합할 수 있다.
 * </p>
 *
 * @see CookieConfigurer
 * @see com.jerocaller.libs.spoonsuits.web.cookie.impl.DefaultCookieConfigurerImpl
 */
@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final CookieConfigurer cookieConfigurer;

    /**
     * <p>새로운 쿠키 추가 메서드.</p>
     * <p>
     *     새롭게 추가하고자 하는 쿠키의 name, value, maxAge 설정은
     *     CookieRequest를 이용한다. 그 외 HttpOnly, Https 여부 등의
     *     나머지 쿠키 설정은 CookieConfigurer를 이용하여 설정한다.
     * </p>
     * <p>
     *     CookieConfigurer에 maxAge를 설정한 경우, CookieRequest의 maxAge를 무시하고
     *     CookieConfigurer에 설정된 maxAge를 우선으로 한다.
     *     다만 CookieConfigurer 내부에 maxAge를 설정하는 경우는 쿠키 사용의 유연성 측면에서
     *     비권장됨.
     * </p>
     *
     * @see CookieConfigurer
     * @param response
     * @param cookieRequest
     */
    public void addCookie(
        HttpServletResponse response,
        CookieRequest cookieRequest
    ) {

        Cookie cookie = new Cookie(
            cookieRequest.getCookieName(),
            cookieRequest.getCookieValue()
        );
        cookie.setMaxAge(cookieRequest.getMaxAge());
        cookieConfigurer.configureCookie(cookie);
        response.addCookie(cookie);

    }

    /**
     * <p>여러 개의 쿠키를 삭제할 수 있는 메서드</p>
     * <p>
     *     삭제하고자 하는 쿠키의 name을 String 형태로 대입한다.
     *     삭제 대상 쿠키의 설정은 CookieConfigurer에서 설정한 그것과 일치하며,
     *     만약 CookieConfigurer에 maxAge를 설정한 경우, 쿠키 삭제를 위해
     *     이 설정은 무시되며, 무조건 maxAge = 0으로 설정된다.
     *     다만 CookieConfigurer 내부에 maxAge를 설정하는 경우는 쿠키 사용의 유연성
     *     측면에서 비권장됨.
     * </p>
     *
     * @param request
     * @param response
     * @param cookieNames 삭제하고자 하는 쿠키들의 name
     */
    public void deleteCookies(
        HttpServletRequest request,
        HttpServletResponse response,
        String... cookieNames
    ) {

        // 사용자 브라우저에 저장된 쿠키가 아예 없을 경우, 불필요한 작업 방지를 위해
        // 메서드를 종료시킨다.
        if (request.getCookies() == null) {
            return;
        }

        // 실제로 존재하지 않는 쿠키 삭제로 인한 불필요한 작업을 방지하기 위해
        // 실제 쿠키들로부터 삭제하고자 하는 쿠키가 있는지 검증한 후에
        // 있으면 삭제하도록 구성함.
        // 대부분의 경우 쿠키의 수가 그리 많지 않다고 가정하여
        // 특정 쿠키 검색 기능으로 인한 성능 하락은 미미하다고 가정.
        for (String cookieName : cookieNames) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    Cookie cookieToDelete = new Cookie(cookieName, null);
                    cookieConfigurer.configureCookie(cookieToDelete);
                    cookieToDelete.setMaxAge(0);
                    response.addCookie(cookieToDelete);
                }
            }
        }

    }

    /**
     * <p>자바 객체를 직렬화하여 쿠키 값에 넣고자 할 때 사용하는 메서드.</p>
     *
     * <p>
     *     이 메서드에서는 자바 객체를 String 타입으로 직렬화만 한다.
     *     따라서 이 값을 쿠키의 값으로 추가하고자 한다면 CookieUtils#addCookie() 메서드를
     *     이용한다.
     * </p>
     *
     * @see CookieUtils#addCookie(HttpServletResponse, CookieRequest)
     * @see CookieUtils#deserialize(Cookie, Class)
     * @param obj 직렬화하고자 하는 자바 객체. 반드시 Serializable 인터페이스를 구현해야 함.
     * @return 자바 객체를 직렬화하여 나온 String 타입의 값
     */
    public String serialize(Object obj) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj));
    }

    /**
     * <p>
     *     CookieUtils#serialze() 메서드를 통해 직렬화된 값이 들어있는 쿠키로부터
     *     해당 값을 역직렬화하여 자바 객체로 반환한다.
     * </p>
     * <p>
     *     내부적으로 ByteArrayInputStream 및 ObjectInputStream을 이용하여 역직렬화함.
     *     역직렬화 대상인 자바 객체에는 반드시 Serializable 인터페이스를 구현하고 있어야 함.
     * </p>
     *
     * @see CookieUtils#serialize(Object)
     * @param cookie
     * @param cls 역직렬화하고자 하는 자바 객체 클래스.
     * @return 역직렬화된 자바 객체
     * @param <T> 역직렬화하고자 하는 자바 객체 타입
     * @throws IOException byte 형태로 변환된 쿠키의 값을 읽고 역직렬화하는 과정에서
     * 문제가 발생한 경우
     * @throws ClassNotFoundException 역직렬화 대상 자바 객체의 클래스를 찾지 못한 경우
     */
    public <T> T deserialize(Cookie cookie, Class<T> cls)
        throws IOException, ClassNotFoundException
    {

        byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
        Object deserializedObj;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);)
        {
            deserializedObj = ois.readObject();
        }

        return cls.cast(deserializedObj);
    }

}
