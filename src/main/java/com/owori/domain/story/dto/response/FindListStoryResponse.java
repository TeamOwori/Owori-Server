package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FindListStoryResponse {
    private Long id;
    private String title;
    private String contents;
    private String image;
    private Integer heartCnt;
    private Integer commentCnt;
    private String writer;
    private LocalDate startDate;
    private LocalDate endDate;
}
