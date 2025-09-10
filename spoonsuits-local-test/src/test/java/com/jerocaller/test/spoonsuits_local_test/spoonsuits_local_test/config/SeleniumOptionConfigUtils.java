package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.chrome.ChromeOptions;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeleniumOptionConfigUtils {

    /**
     * <p>
     *     Selenium을 이용한 모든 테스트에 적용할 Chrome option.
     * </p>
     * <p>
     *     여기서는 GUI를 지원하지 않는 리눅스와 같은 OS 환경에서도 Selenium 테스트가 진행될 수 있도록
     *     브라우저 GUI 창 띄우기를 방지하도록 설정함.
     * </p>
     *
     * @return
     */
    public static ChromeOptions getChromeOption() {
        ChromeOptions chromeOptions = new ChromeOptions();

        // 헤드리스 모드 활성화. 셀레니움 테스트 실행 시 브라우저 화면을 화면에 띄우지 않고,
        // 메모리 상에서만 실행하는 방식. 특히 GUI를 지원하지 않는 리눅스 등의 OS 환경에서 사용.
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        // 리눅스와 같은 OS 환경에서 공유 메모리(`/dev/shm`) 공간 사용 시
        // 해당 공간이 매우 적게 할당되어 Chrome이 차지하는 메모리 용량이 이를 넘길 경우 문제가 발생할 수 있음.
        // 따라서 해당 공유 메모리 사용을 하지 않고 일반 파일 시스템(`/tmp`)을 사용하도록 함.
        chromeOptions.addArguments("--disable-dev-shm-usage");

        return chromeOptions;
    }
}
