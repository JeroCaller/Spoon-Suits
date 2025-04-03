package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.constant.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileEntity {

    private String name;
    private FileType fileType;

}
