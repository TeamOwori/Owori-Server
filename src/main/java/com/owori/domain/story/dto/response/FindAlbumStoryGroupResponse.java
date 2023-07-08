package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindAlbumStoryGroupResponse {
    private String yearMonth;
    private List<FindAlbumStoryResponse> stories;
    private boolean hasNext;
    public FindAlbumStoryGroupResponse(String yearMonth){
        this.yearMonth = yearMonth;
    }

    public void updateStories(List<FindAlbumStoryResponse> stories, boolean hasNext){
        this.stories = stories;
        this.hasNext = hasNext;
    }
}
