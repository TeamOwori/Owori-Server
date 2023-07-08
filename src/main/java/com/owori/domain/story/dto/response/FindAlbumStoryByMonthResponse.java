package com.owori.domain.story.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindAlbumStoryByMonthResponse {
    private String yearMonth;
    private List<FindAlbumStoryResponse> stories;
    public FindAlbumStoryByMonthResponse(String yearMonth){
        this.yearMonth = yearMonth;
    }

    public void updateStories(List<FindAlbumStoryResponse> stories){
        this.stories = stories;
    }
}
