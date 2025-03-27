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

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final CookieConfigurer cookieConfigurer;

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
     * 객체 직렬화 후 쿠키의 값으로 변환
     *
     * @param obj
     * @return
     */
    public String serialize(Object obj) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj));
    }

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
