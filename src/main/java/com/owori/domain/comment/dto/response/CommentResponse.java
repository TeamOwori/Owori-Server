package com.owori.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long parentCommentId;
    private Long commentId;
    private String comment;
    private String writer;
    private String timeBeforeWriting;
}