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
    private UUID storyId;
    private String title;
    private String content;
    private String thumbnail;
    private Boolean isMultipleImages;
    private Integer heartCount;
    private Integer commentCount;
    private String writer;
    private LocalDate startDate;
    private LocalDate endDate;
}
