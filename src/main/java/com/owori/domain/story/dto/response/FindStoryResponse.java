package com.owori.domain.story.dto.response;

import com.owori.domain.comment.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindStoryResponse {
    private UUID id;
    private Boolean isLiked;
    private List<String> images;
    private String title;
    private String writer;
    private String contents;
    private Long HeartCnt;
    private Long CommentCnt;
    private List<CommentResponse> comments;
}
