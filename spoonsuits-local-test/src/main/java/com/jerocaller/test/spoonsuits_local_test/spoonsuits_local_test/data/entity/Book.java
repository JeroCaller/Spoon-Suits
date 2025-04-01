package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private String title;
    private Integer pages;
    private Integer price;
    private String genre;

}
