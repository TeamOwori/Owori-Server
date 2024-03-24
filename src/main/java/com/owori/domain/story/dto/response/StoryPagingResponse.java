package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryPagingResponse {
    private List<FindAllStoryResponse> stories = new ArrayList<>();
    private boolean hasNext;
}
