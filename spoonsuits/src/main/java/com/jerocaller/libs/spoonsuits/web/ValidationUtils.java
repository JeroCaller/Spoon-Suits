package com.jerocaller.libs.spoonsuits.web;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>유효성 검사 유틸리티 클래스</p>
 *
 * <p>
 *     이 클래스 내 모든 메서드들은 static으로 선언되어 있기에
 *     별도로 Spring Bean으로 등록하는 과정은 불필요하다.
 * </p>
 */
public class ValidationUtils {

    /**
     * <p>
     *     유효성 검사에 실패한 필드들의 정보만 추출하여
     *     <code>필드명: 유효성 검사 실패 메시지</code>의 Map 타입으로 반환하는 메서드.
     * </p>
     * <p>
     *     REST API에서 특정 필드들의 유효성 검사 실패 시 응답 JSON에 포함시킬 목적으로
     *     만든 메서드.
     * </p>
     *
     * @param exception 유효성 검사 실패로 발생한 MethodArgumentNotValidException 예외 객체
     * @return <code>필드명: 유효성 검사 실패 메시지</code> 형태의 Map 타입
     */
    public static Map<String, String> getValidationFailedMessage(
        MethodArgumentNotValidException exception
    ) {

        Map<String, String> validationFailedMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            validationFailedMap.put(e.getField(), e.getDefaultMessage());
        });

        return validationFailedMap;
    }

}
