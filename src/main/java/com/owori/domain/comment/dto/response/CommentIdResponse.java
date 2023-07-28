package com.owori.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CommentIdResponse {
    private UUID commentId;
}
