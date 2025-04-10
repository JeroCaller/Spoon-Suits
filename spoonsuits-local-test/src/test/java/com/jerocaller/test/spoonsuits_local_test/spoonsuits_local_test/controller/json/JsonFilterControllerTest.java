package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.json;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config.TestSecurityDisableConfig;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller.FileController;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
@Import({FileService.class})
@ContextConfiguration(classes = {TestSecurityDisableConfig.class})
@Slf4j
public class JsonFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String UPLOAD_URI = "/test/files/mode/empty/null";
    private final String DOWNLOAD_URI = "/test/files/mode/empty/zero";

    @Test
    @DisplayName("테스트 클래스 실행 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트 클래스가 잘 실행되고 있다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("""
        여러 파일 업로드 시 업로드에 실패한 파일이 없다면
        응답 json에 failedFiles 프로퍼티가 없어야 한다.
    """)
    void jsonFilteredDueToNoFileFailedToUploadTest() throws Exception {

        final String numOfFailed = "0";

        mockMvc.perform(post(UPLOAD_URI)
            .param("numOfFailed", numOfFailed)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message")
                .value("모든 파일 업로드 성공")
            )
            .andExpect(jsonPath("$.failedFiles").doesNotExist())
            .andExpect(jsonPath("$.failedFiles").doesNotHaveJsonPath())
            .andDo(print());

    }

    @Test
    @DisplayName("""
        여러 파일 업로드 시 한 개 이상의 업로드 실패한 파일이 있다면 
        응답 json 프로퍼티에 failedFiles 프로퍼티가 존재해야 한다.
    """)
    void noJsonFilteredDueToThereAreFilesFailedToUploadTest() throws Exception {

        final String numOfFailed = "1";

        mockMvc.perform(post(UPLOAD_URI)
            .param("numOfFailed", numOfFailed)
        )
            .andExpect(status().isPartialContent())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.message")
                .value("파일들의 일부 또는 전체가 업로드에 실패했습니다.")
            )
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.failedFiles").exists())
            .andExpect(jsonPath("$.failedFiles").hasJsonPath())
            .andExpect(jsonPath("$.failedFiles[0]").exists())
            .andDo(print());

    }

    @Test
    @DisplayName("""
        여러 파일 다운로드 시 실패한 파일이 없다면 
        응답 Json에 failedFiles 프로퍼티는 없어야 한다.
    """)
    void jsonFilteredDueToNoFilesFailedToDownloadTest() throws Exception {

        final String numOfFailed = "0";

        mockMvc.perform(get(DOWNLOAD_URI)
            .param("numOfFailed", numOfFailed)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.failedFiles").doesNotExist())
            .andExpect(jsonPath("$.failedFiles").doesNotHaveJsonPath())
            .andDo(print());

    }

    @Test
    @DisplayName("""
        여러 파일 다운로드 중 한 개 이상의 파일들이 다운로드에 실패하면 
        응답 Json에 failedFiles 프로퍼티가 존재해야 한다.
    """)
    void noJsonFilteredDueToFilesFailedToDownloadTest() throws Exception {

        final String numOfFailed = "2";

        mockMvc.perform(get(DOWNLOAD_URI)
            .param("numOfFailed", numOfFailed)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.failedFiles").exists())
            .andExpect(jsonPath("$.failedFiles").hasJsonPath())
            .andDo(print());

    }

}
