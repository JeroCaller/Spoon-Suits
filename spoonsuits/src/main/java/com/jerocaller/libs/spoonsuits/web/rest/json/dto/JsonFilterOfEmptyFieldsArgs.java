package com.jerocaller.libs.spoonsuits.web.rest.json.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonFilterOfEmptyFieldsArgs<T> {

    private T pojoForRestResponse;
    private String jsonFilterName;
    private List<String> targetFieldNames;

}
