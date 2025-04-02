package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.constant.FileType;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.file.FileResultDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.FileEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    public FileResultDto uploadFiles(int numOfFailed) {

        FileResultDto fileResultDto = new FileResultDto();
        List<FileEntity> mockFiles = Arrays.asList(
            FileEntity.builder()
                .name("밤하늘.png")
                .fileType(FileType.IMAGE)
                .build(),
            FileEntity.builder()
                .name("채팅 생성형 AI 잘 다루는 법.mp4")
                .fileType(FileType.VIDEO)
                .build(),
            FileEntity.builder()
                .name("README.md")
                .fileType(FileType.TEXT)
                .build(),
            FileEntity.builder()
                .name("screenshot-20250403.jpeg")
                .fileType(FileType.IMAGE)
                .build(),
            FileEntity.builder()
                .name("a.jpg")
                .fileType(FileType.IMAGE)
                .build(),
            FileEntity.builder()
                .name("잠 잘자는 법.txt")
                .fileType(FileType.TEXT)
                .build()
        );

        if (numOfFailed > mockFiles.size()) {
            numOfFailed = mockFiles.size();
        }

        int endOfSuccess = mockFiles.size() - numOfFailed;
        for (int i = 0; i < endOfSuccess; ++i) {
            fileResultDto.getSuccessFiles().add(mockFiles.get(i));
        }

        for (int j = endOfSuccess; j < mockFiles.size(); ++j) {
            fileResultDto.getFailedFiles().add(mockFiles.get(j));
        }

        return fileResultDto;
    }

}
