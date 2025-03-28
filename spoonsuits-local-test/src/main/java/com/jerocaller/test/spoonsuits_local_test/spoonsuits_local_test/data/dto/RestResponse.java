package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * REST API에서 JSON 형태의 응답을 위한 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestResponse {

    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public ResponseEntity<RestResponse> toResponseEntity() {
        return ResponseEntity.status(this.getHttpStatus())
            .body(this);
    }

}
