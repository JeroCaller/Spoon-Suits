package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.repository;

import com.jerocaller.libs.spoonsuits.web.jpa.PageUtils;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Festival;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.repository.FestivalRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class PageUtilsTest {

    @Autowired
    private FestivalRepository festivalRepository;

    @Test
    @DisplayName("컨텍스트 로딩 여부 확인")
    void initTest() {
        log.info("이 메시지가 보인다면 테스트에 필요한 컨텍스트 로딩에 성공했다는 뜻입니다.");
        log.info(this.getClass().getName());
    }

    @Test
    @DisplayName("기본 PageRequest.of() 메서드는 zero-based index이다.")
    void oldPageRequestIsZeroBasedIndexTest() {

        Pageable pageRequest = PageRequest.of(0, 10);
        Page<Festival> festivals = festivalRepository.findAll(pageRequest);
        List<Festival> festivalsList = festivals.getContent();

        // page number이 0이므로 첫 번째 페이지에는 id가 1 ~ 10인 데이터들이 존재한다.
        assertThat(festivalsList).contains(Festival.builder().id(1).build());
        assertThat(festivalsList).contains(Festival.builder().id(10).build());
        assertThat(festivalsList)
            .doesNotContain(Festival.builder().id(11).build());

        logPageInfo(festivals);

    }

    @Test
    @DisplayName("PageUtils.getPageRequsetOf()의 one-based index 여부 확인")
    void pageUtilsHasOneBasedIndexSystemTest() {

        Pageable pageRequest = PageUtils.getPageRequestOf(1, 10);
        Page<Festival> festivals = festivalRepository.findAll(pageRequest);
        List<Festival> festivalList = festivals.getContent();

        assertThat(festivals.getNumber()).isZero();
        assertThat(festivalList).contains(Festival.builder().id(1).build());
        assertThat(festivalList).contains(Festival.builder().id(10).build());
        assertThat(festivalList)
            .doesNotContain(Festival.builder().id(11).build());

        logPageInfo(festivals);

    }

    @Test
    @DisplayName("PageUtils.isEmpty() - Page 요소들의 개수가 0 또는 null일 때 true 반환 여부")
    void isEmptyTrueTest() {

        Page<Festival> nullFestival = null;
        assertThat(PageUtils.isEmtpy(nullFestival)).isTrue();

        // 존재하지 않는 페이지 요청
        Pageable pageRequest = PageRequest.of(10, 10);
        Page<Festival> zeroFestival = festivalRepository.findAll(pageRequest);

        assertThat(zeroFestival.getNumberOfElements()).isZero();
        assertThat(PageUtils.isEmtpy(zeroFestival)).isTrue();

        logPageInfo(zeroFestival);

    }

    @Test
    @DisplayName("""
       PageUtils.isEmpty() - Page가 non null이거나 크기가 0이 아닌 경우
       false 반환 여부 확인
    """)
    void isEmptyFalseTest() {

        Pageable pageRequest = PageRequest.of(1, 10);
        Page<Festival> festivals = festivalRepository.findAll(pageRequest);

        assertThat(festivals.getNumberOfElements()).isNotZero();
        assertThat(PageUtils.isEmtpy(festivals)).isFalse();

        logPageInfo(festivals);

    }

    private <T> void logPageInfo(Page<T> pages) {

        if (pages == null) {
            log.info("주어진 페이지가 null입니다.");
            return;
        }

        log.info("전체 페이지 수: {}", pages.getTotalPages());
        log.info("전체 데이터 수: {}", pages.getTotalElements());
        log.info("현재 페이지 번호(zero-based index): {}", pages.getNumber());
        log.info("현재 페이지의 크기: {}", pages.getNumberOfElements());
        log.info("다음 페이지 존재 여부: {}", pages.hasNext());
        log.info("이전 페이지 존재 여부: {}", pages.hasPrevious());

    }

}
