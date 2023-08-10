package com.owori.domain.story.dto.response;

import com.owori.domain.comment.dto.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FindStoryResponse {
    private UUID storyId;
    private Boolean isLiked;
    private List<String> storyImages;
    private String title;
    private String writer;
    private String content;
    private Integer heartCount;
    private Integer commentCount;
    private List<CommentResponse> comments;
    private LocalDate startDate;
    private LocalDate endDate;
    private String thumbnail;
}
