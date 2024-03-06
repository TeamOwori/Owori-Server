package com.owori.domain.story.dto.response;

import com.owori.domain.story.entity.Story;
import com.owori.domain.story.mapper.StoryMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoryPagingResponse {
    private static final StoryMapper mapper = new StoryMapper();
    private List<FindAllStoryResponse> stories = new ArrayList<>();
    private boolean hasNext;

    public static StoryPagingResponse of(Page<Story> pageStory){
        return new StoryPagingResponse(getContents(pageStory.getContent()), pageStory.hasNext());
    }

    private static List<FindAllStoryResponse> getContents(List<Story> stories){
        return stories.stream().map(story -> mapper.toFindAllStoryResponse(story)).toList();
    }

}
