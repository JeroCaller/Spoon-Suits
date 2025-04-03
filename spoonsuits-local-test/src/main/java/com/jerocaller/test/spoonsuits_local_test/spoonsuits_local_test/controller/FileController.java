package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.CustomRestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.file.FileResultDto;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     REST API의 응답 JSON에 대한 테스트를 위한 컨트롤러 클래스.
 * </p>
 * <p>
 *     Spring Security와는 관련 없으므로 permitAll 처리한다.
 * </p>
 */
@RestController
@RequestMapping("/test/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/mode/empty/null")
    public ResponseEntity<Object> uploadFilesAndNullIfNoFailed(
        @RequestParam("numOfFailed") int numOfFailed
    ) {

        CustomRestResponse restResponse = null;
        FileResultDto fileResultDto = fileService.uploadFiles(numOfFailed);

        if (!fileResultDto.getFailedFiles().isEmpty()) {
            restResponse = CustomRestResponse.builder()
                .httpStatus(HttpStatus.PARTIAL_CONTENT)
                .message("파일들의 일부 또는 전체가 업로드에 실패했습니다.")
                .failedFiles(fileResultDto.getFailedFiles())
                .data(fileResultDto.getSuccessFiles())
                .build();
        } else {
            // CustomRestResponse.failedFiled = null;
            restResponse = CustomRestResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("모든 파일 업로드 성공")
                .data(fileResultDto.getSuccessFiles())
                .build();
        }

        return restResponse.toResponseEntity();
    }

    @GetMapping("/mode/empty/zero")
    public ResponseEntity<Object> downloadFilesAndZeroIfNoFailed(
        @RequestParam("numOfFailed") int numOfFailed
    ) {

        FileResultDto fileResultDto = fileService.uploadFiles(numOfFailed);

        return CustomRestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("여러 파일들에 대한 다운로드 실행 완료")
            // 작업 실패 파일이 하나도 없다면 결국 사이즈가 0인 빈 리스트가 대입됨.
            // If no Failed Files, CustomRestResponse.failedFiles = [];
            .failedFiles(fileResultDto.getFailedFiles())
            .data(fileResultDto.getSuccessFiles())
            .build()
            .toResponseEntity();
    }

}
