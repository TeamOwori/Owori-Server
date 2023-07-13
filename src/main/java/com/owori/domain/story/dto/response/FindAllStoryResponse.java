package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAllStoryResponse {
    private UUID id;
    private String title;
    private String contents;
    private String image;
    private Integer heartCnt;
    private Integer commentCnt;
    private String writer;
    private LocalDate startDate;
    private LocalDate endDate;
}
