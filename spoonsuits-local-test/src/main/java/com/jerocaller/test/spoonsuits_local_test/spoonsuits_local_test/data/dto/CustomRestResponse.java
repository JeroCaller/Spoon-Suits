package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.jerocaller.libs.spoonsuits.web.rest.json.JsonFilterOfEmptyFields;
import com.jerocaller.libs.spoonsuits.web.rest.json.dto.JsonFilterOfEmptyFieldsArgs;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFilter("customRestResponseFilter")
public class CustomRestResponse {

    private HttpStatus httpStatus;
    private String message;
    private Object data;
    private List<FileEntity> failedFiles;

    public ResponseEntity<Object> toResponseEntity() {

        JsonFilterOfEmptyFields jsonFilter = new JsonFilterOfEmptyFields();
        JsonFilterOfEmptyFieldsArgs args = JsonFilterOfEmptyFieldsArgs.builder()
            .pojoForRestResponse(this)
            .jsonFilterName("customRestResponseFilter")
            .targetFieldNames(List.of("failedFiles"))
            .build();

        return ResponseEntity.status(this.httpStatus)
            .body(jsonFilter.getJsonWithEmptyFieldsFilteredOut(args));
    }

}
