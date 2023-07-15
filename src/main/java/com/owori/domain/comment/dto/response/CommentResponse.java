package com.owori.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {
    private UUID parentCommentId;
    private UUID commentId;
    private String comment;
    private String writer;
    private String timeBeforeWriting;
}
