package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.controller;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.member.MemberWithTokenResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto.RestResponse;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Book;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.AuthService;
import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>테스트용 도서 데이터 조회용 컨트롤러.</p>
 * <p>
 *     인증 테스트도 포함되어 있으므로 이 컨트롤러의 URI들은
 *     SecurityConfig에 PermitAll에 등록하지 않는다.
 * </p>
 *
 */
@RestController
@RequestMapping("/test/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<RestResponse> getAllBookes() {

        List<Book> books = bookService.getAllBooks();

        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("조회 성공")
            .data(books)
            .build()
            .toResponseEntity();
    }

    /**
     * <p>
     *     모든 책 조회와 동시에 현재 인증된 사용자의
     *     정보도 반환.
     * </p>
     * <p>
     *     <code>SecurityContextHolder.getContext.getAuthentication()</code>에
     *     인증된 현재 사용자 정보가 있는지 확인용.
     * </p>
     *
     * @return
     */
    @GetMapping("/with-my-info")
    public ResponseEntity<RestResponse> getAllBooksWithMyInfo() {

        List<Book> books = bookService.getAllBooks();
        MemberWithTokenResponse currentMember = authService.
            getCurrentAuthInfo();

        Map<String, Object> allData = new HashMap<>();
        allData.put("books", books);
        allData.put("user", currentMember);

        return RestResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("모든 서적 및 현재 사용자 정보 조회 성공")
            .data(allData)
            .build()
            .toResponseEntity();
    }

}
