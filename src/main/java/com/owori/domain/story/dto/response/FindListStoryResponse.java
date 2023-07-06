package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindListStoryResponse {
    private String title;
    private String contents;
    private String image;
    private Long heartCnt;
    private Long commentCnt;
    private String writer;
    private String date;
}
