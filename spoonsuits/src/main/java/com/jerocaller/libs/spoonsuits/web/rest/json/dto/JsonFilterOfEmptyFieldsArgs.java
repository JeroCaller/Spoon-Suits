package com.jerocaller.libs.spoonsuits.web.rest.json.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 *     REST API 응답 JSON 데이터로의 직렬화 대상인 자바 객체에 빈 값이 들어 있는
 *     특정 필드들을 직렬화 대상에서 제외시키기 위해 해당 대상들을 지정하는 DTO 클래스.
 * </p>
 *
 * @see com.jerocaller.libs.spoonsuits.web.rest.json.JsonFilterOfEmptyFields
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JsonFilterOfEmptyFieldsArgs {

    /**
     * <p>직렬화 대상 자바 객체</p>
     */
    private Object pojoForRestResponse;

    /**
     * <p>
     *     {@code @JsonFilter}에 적힌 문자열 형태의 json filter 이름.
     * </p>
     */
    private String jsonFilterName;

    /**
     * <p>
     *     필터 대상 필드 이름들.
     * </p>
     */
    private List<String> targetFieldNames;

}
