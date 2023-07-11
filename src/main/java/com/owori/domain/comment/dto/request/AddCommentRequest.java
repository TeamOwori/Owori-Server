package com.owori.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest {
    private Long StoryId;
    private UUID parentCommentId;
    private String content;
}
