package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    IMAGE("image"),
    VIDEO("video"),
    TEXT("text");

    private String typeName;

}
