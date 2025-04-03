package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.file;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.FileEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     업로드, 다운로드 등의 여러 파일들의 작업들에 대해,
 *     작업에 성공한 파일들과 실패한 파일들의 정보를 담는 DTO 클래스.
 * </p>
 */
@Getter
public class FileResultDto {

    private final List<FileEntity> successFiles = new ArrayList<>();
    private final List<FileEntity> failedFiles = new ArrayList<>();

    public void clearAll() {
        successFiles.clear();
        failedFiles.clear();;
    }

}
