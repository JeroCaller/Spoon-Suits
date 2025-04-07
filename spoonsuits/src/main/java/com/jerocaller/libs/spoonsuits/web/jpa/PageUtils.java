package com.jerocaller.libs.spoonsuits.web.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * <p>
 *     Spring Data JPA의 Page 관련 유틸리티 클래스.
 * </p>
 * <p>
 *     이 클래스 내 모든 메서드들은 static으로 선언되었으므로
 *     굳이 Spring Bean에 등록하여 사용하지 않고도 바로 사용할 수 있음.
 * </p>
 */
public class PageUtils {

    /**
     * <p>
     *     페이지의 시작 번호를 0에서 1로 바꾼 페이지 요청 객체 PageRequest를 반환한다.
     * </p>
     * <p>
     *     <code>PageRequest.of(page, size)</code>에 들어가는 페이지 번호는 zero-based이다.
     *     즉, 페이지 번호를 1 입력하면 내부적으로는 2번 페이지로 인식된다.
     *     0을 입력해야 1번째 페이지로 입력됨.
     *     그러나 현실에서는 보통 페이지 번호를 1부터 시작한다.
     *     이러한 혼란을 없애기 위해 one-based의 메서드로 재정의함.
     * </p>
     *
     * @param page 1부터 시작하는 페이지 번호. 범위는 <code>page >= 1</code>이어야 함.
     * @param size 한 페이지의 크기. 한 페이지에 담을 데이터의 수.
     * @return
     */
    public static Pageable getPageRequestOf(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    /**
     * <p>
     *     페이지의 시작 번호를 0에서 1로 바꾼 페이지 요청 객체 PageRequest를 반환한다.
     * </p>
     * <p>
     *     <code>PageRequest.of(page, size)</code>에 들어가는 페이지 번호는 zero-based이다.
     *     즉, 페이지 번호를 1 입력하면 내부적으로는 2번 페이지로 인식된다.
     *     0을 입력해야 1번째 페이지로 입력됨.
     *     그러나 현실에서는 보통 페이지 번호를 1부터 시작한다.
     *     이러한 혼란을 없애기 위해 one-based의 메서드로 재정의함.
     * </p>
     * @param page 1부터 시작하는 페이지 번호. 범위는 <code>page >= 1</code>이어야 함.
     * @param size 한 페이지의 크기. 한 페이지에 담을 요소들의 수.
     * @param sort
     * @return
     */
    public static Pageable getPageRequestOf(
        int page,
        int size,
        Sort sort
    ) {
        return PageRequest.of(page - 1, size, sort);
    }

    /**
     * <p>현재 Page내 요소들의 개수가 0 또는 null인지 판별</p>
     *
     * @param <T> Page 객체가 담고 있는 객체 타입
     * @param pages
     * @return
     */
    public static <T> boolean isEmtpy(Page<T> pages) {
        return (pages == null || pages.getNumberOfElements() == 0);
    }

}
