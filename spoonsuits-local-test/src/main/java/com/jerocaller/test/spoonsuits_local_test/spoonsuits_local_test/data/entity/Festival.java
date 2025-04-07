package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "fake_festival")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id"})
public class Festival {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(nullable = false, length = 100, name = "TITLE")
    private String title;

    @Column(nullable = false, length = 100, name = "ORIGIN")
    private String origin;

    @Column(nullable = false, length = 1000, name = "CONTENT")
    private String content;

    @Column(nullable = false, length = 50, name = "IMAGE")
    private String image;

    @Column(nullable = false, name = "STARTDATE")
    private LocalDate startDate;

    @Column(nullable = false, name = "ENDDATE")
    private LocalDate endDate;

}
