package com.owori.domain.story.dto.collection;

import com.owori.domain.image.entity.Image;
import com.owori.domain.story.dto.response.FindAlbumStoryGroupResponse;
import com.owori.domain.story.dto.response.FindAlbumStoryResponse;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public class StoryAlbumGroup {
    private final Map<String, List<Story>> storyByYearMonth;

    public StoryAlbumGroup(Map<String, List<Story>> storyByYearMonth) {
        this.storyByYearMonth = storyByYearMonth;
    }

    public List<FindAlbumStoryGroupResponse> getStoryGroupResponses(Slice<Story> storyBySlice) {
        return storyByYearMonth.entrySet().stream()
                .map(entry -> {
                    FindAlbumStoryGroupResponse storyGroupResponse = new FindAlbumStoryGroupResponse(entry.getKey());
                    List<FindAlbumStoryResponse> storyResponseList = entry.getValue().stream()
                            .map(story -> {
                                String imageUrl = story.getImages().stream()
                                        .findFirst().map(Image::getUrl).orElse(null);
                                return new FindAlbumStoryResponse(story.getId(), imageUrl);
                            }).toList();
                    storyGroupResponse.updateStories(storyResponseList, storyBySlice.hasNext());
                    return storyGroupResponse;
                }).toList();
    }
}
