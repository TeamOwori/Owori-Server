package com.owori.domain.story.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class FindAllStoryResponse {
    private Long id;
    private String title;
    private String contents;
    private String image;
    private Integer heartCnt;
    private Integer commentCnt;
    private String writer;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public FindAllStoryResponse(Long id, String title, String contents, String image, Integer heartCnt,
                                Integer commentCnt, String writer, LocalDate startDate, LocalDate endDate){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
        this.writer = writer;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
