package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindStoryResponse {
    private Long id;
    private Boolean isLiked;
    private List<String> images;
    private String title;
    private String writer;
    private String contents;
    private Long HeartCnt;
    private Long CommentCnt;
    private List<CommentResponse> comments;
}
