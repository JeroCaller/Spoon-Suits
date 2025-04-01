package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.service;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    public List<Book> getAllBooks() {
        return Arrays.asList(
            Book.builder()
                .title("자바 타파하기")
                .genre("IT")
                .pages(300)
                .price(12000)
                .build(),
            Book.builder()
                .title("파이썬 독학하기")
                .genre("IT")
                .pages(400)
                .price(15000)
                .build(),
            Book.builder()
                .title("자취 요리 레시피 모음")
                .genre("요리")
                .pages(200)
                .price(20000)
                .build()
        );
    }

}
