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

// TODO : 테스트 코드 작성 및 테스트 진행.
// TODO : 테스트 완료 후 javadoc 작성
public class JsonFilterOfEmptyFields {
    
    public MappingJacksonValue getJsonPojoWithEmptyFieldsFilteredOut(
        JsonFilterOfEmptyFieldsArgs<?> argsDto
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
        JsonFilterOfEmptyFieldsArgs<?> argsDto
    ) {

        List<String> fieldNamesToExclude = new ArrayList<>();

        for (String fieldName : argsDto.getTargetFieldNames()) {
            Field field = null; // field 정보에 접근하기 위함.
            Object fieldValue = null; // field에 할당된 값

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
