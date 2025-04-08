package com.jerocaller.libs.spoonsuits.web.rest.json;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jerocaller.libs.spoonsuits.web.rest.json.dto.JsonFilterOfEmptyFieldsArgs;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     REST API 응답용 JSON으로 직렬화될 자바 객체 내 그 값이 null이거나
 *     size가 0인 특정 필드들을 직렬화 대상에서 제외한다.
 * </p>
 * <p>
 *     값이 없거나 비어있는 특정 필드들이 직렬화 대상에서 제외된다는 것은
 *     해당 필드 자체가 아예 JSON 데이터 내 프로퍼티에 포함되지 않는다는 뜻이다.
 * </p>
 *
 * <p>사용 예시)</p>
 * <pre>{@code
 * @Getter
 * @NoArgsConstructor
 * @AllArgsConstructor
 * @Builder
 * @JsonFilter("customRestResponseFilter")  // Json 필터 대상에 이 어노테이션을 부여.
 * public class CustomRestResponse {
 *
 *     private HttpStatus httpStatus;
 *     private String message;
 *     private Object data;
 *     private List<FileEntity> failedFiles; // null 또는 빈 값일 시 필터 대상.
 *
 *     public ResponseEntity<Object> toResponseEntity() {
 *         // JsonFilterOfEmptyFields 유틸 사용.
 *         JsonFilterOfEmptyFields jsonFilter = new JsonFilterOfEmptyFields();
 *         JsonFilterOfEmptyFieldsArgs args = JsonFilterOfEmptyFieldsArgs.builder()
 *             .pojoForRestResponse(this) // json filter를 적용할 대상 객체
 *             .jsonFilterName("customRestResponseFilter") // @JsonFilter 이름.
 *             .targetFieldNames(List.of("failedFiles")) // 빈 값 존재 시 직렬화에서 제외할 필드명
 *             .build();
 *
 *         return ResponseEntity.status(this.httpStatus)
 *             .body(jsonFilter.getJsonWithEmptyFieldsFilteredOut(args));
 *     }
 *
 * }
 * }</pre>
 * <pre>{@code
 * // REST Controller
 * @GetMapping("/mode/empty/zero")
 * public ResponseEntity<Object> downloadFilesAndZeroIfNoFailed(
 *     @RequestParam("numOfFailed") int numOfFailed
 * ) {
 *
 *     FileResultDto fileResultDto = fileService.uploadFiles(numOfFailed);
 *
 *     return CustomRestResponse.builder()
 *         .httpStatus(HttpStatus.OK)
 *         .message("여러 파일들에 대한 다운로드 실행 완료")
 *         // 작업 실패 파일이 하나도 없다면 결국 사이즈가 0인 빈 리스트가 대입됨.
 *         // If no Failed Files, CustomRestResponse.failedFiles = [];
 *         .failedFiles(fileResultDto.getFailedFiles())
 *         .data(fileResultDto.getSuccessFiles())
 *         .build()
 *         .toResponseEntity();
 * }
 * }</pre>
 *
 * <pre><code>
 *     HTTP Request
 *     GET /mode/empty/zero?numOfFailed=0
 *
 *     HTTP Response
 *     {
 *         "httpStatus": "OK",
 *         "message": "여러 파일들에 대한 다운로드 실행 완료",
 *         "data": [...]
 *         // "failedFiles": null  => 필터링에 의해 존재하지 않음.
 *     }
 * </code></pre>
 *
 * <pre><code>
 *     HTTP Request
 *     GET /mode/empty/zero?numOfFailed=1
 *
 *     HTTP Response
 *     {
 *         "httpStatus": "OK",
 *         "message": "여러 파일들에 대한 다운로드 실행 완료",
 *         "data": [...]
 *         "failedFiles": [...]  // 값이 존재하므로 응답 JSON에도 포함됨
 *     }
 * </code></pre>
 * <p>
 *     직렬화 대상 자바 객체 내 특정 필드의 값이 null이거나 사이즈가 0일 때 원래라면
 *     <code>"failedFiles": null</code> 또는 <code>"failedFiles": []</code>
 *     등의 형태로 응답 JSON 데이터에 보일 것이다. 만약 빈 값일 경우 해당 필드 자체가
 *     응답 JSON에 포함되지 않고자 할 때 사용한다.
 * </p>
 *
 * <b>Note)</b>
 * <ul>
 *     <li>
 *         이 클래스는 기본적으로 Spring Bean으로 등록하지 않았으므로 상황에 따라
 *         new 키워드를 이용해 객체를 직접 생성하여 사용 또는
 *         <code>@Configuration</code> 설정 클래스에 <code>@Bean</code>을 이용하여
 *         스프링 빈으로 등록하고 의존성 주입하여 사용해도 됨.
 *     </li>
 *     <li>
 *         위 사용 예시에서도 볼 수 있듯, 이 기능을 사용하려면 필터링할 직렬화될 자바
 *         클래스에 {@code @JsonFilter} 어노테이션을 적용해야한다.
 *     </li>
 * </ul>
 *
 * @see com.fasterxml.jackson.annotation.JsonFilter
 */
public class JsonFilterOfEmptyFields {

    /**
     * <p>
     *     JSON 응답 데이터로의 직렬화 대상 자바 객체에 필터링을 적용한 후의 자바 객체를
     *     반환.
     * </p>
     * <p>
     *     직렬화 대상 자바 클래스에
     *     {@link com.fasterxml.jackson.annotation.JsonFilter} 어노테이션을
     *     반드시 적용해야함.
     * </p>
     *
     * @param argsDto
     * @return
     */
    public MappingJacksonValue getJsonWithEmptyFieldsFilteredOut(
        JsonFilterOfEmptyFieldsArgs argsDto
    ) {
        
        SimpleBeanPropertyFilter filter = getFilterWithNonEmptyFields(argsDto);
        FilterProvider filterProvider = new SimpleFilterProvider()
            .addFilter(argsDto.getJsonFilterName(), filter);

        MappingJacksonValue mappingJacksonValue =
            new MappingJacksonValue(argsDto.getPojoForRestResponse());
        mappingJacksonValue.setFilters(filterProvider);
        
        return mappingJacksonValue;
    }
    
    protected SimpleBeanPropertyFilter getFilterWithNonEmptyFields(
        JsonFilterOfEmptyFieldsArgs argsDto
    ) {

        List<String> fieldNamesToExclude = new ArrayList<>();

        for (String fieldName : argsDto.getTargetFieldNames()) {
            Field field; // field 정보에 접근하기 위함.
            Object fieldValue; // field에 할당된 값

            try {
                field = argsDto.getPojoForRestResponse()
                    .getClass()
                    .getDeclaredField(fieldName);
                field.setAccessible(true); // private 필드에도 접근 허용.
                fieldValue = field.get(argsDto.getPojoForRestResponse());
            } catch (Exception e) {
                // 특정 필드가 존재하지 않는다고 가정하고 작업을 건너뛴다.
                continue;
            }

            // 가져온 필드의 값이 null 또는 그 크기가 0일 경우
            // JSON 응답 데이터 제외 대상 목록에 등록.
            if (fieldValue == null) {
                fieldNamesToExclude.add(fieldName);
            } else if (fieldValue instanceof Map<?, ?> fieldMap) {
                if (fieldMap.isEmpty()) {
                    fieldNamesToExclude.add(fieldName);
                }
            } else if (fieldValue instanceof List<?> fieldList) {
                if (fieldList.isEmpty()) {
                    fieldNamesToExclude.add(fieldName);
                }
            }
        }
        
        return SimpleBeanPropertyFilter.serializeAllExcept(
            fieldNamesToExclude.toArray(new String[0])
        );
    }
    
}
