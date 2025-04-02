package com.jerocaller.libs.spoonsuits.web.rest.json.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JsonFilterOfEmptyFieldsArgs {

    private Object pojoForRestResponse;
    private String jsonFilterName;
    private List<String> targetFieldNames;

}
