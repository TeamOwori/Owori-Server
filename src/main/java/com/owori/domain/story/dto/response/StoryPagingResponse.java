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
    private static final StoryMapper storyMapper = new StoryMapper();
    private static final Integer LAST_PAGE = -1;

    private List<FindAllStoryResponse> contents = new ArrayList<>();
    private Integer nextPage;
    private Integer lastPage;



    public static StoryPagingResponse of(Page<Story> pageStory){
        if(!pageStory.hasNext()){
            return StoryPagingResponse.newLastScroll(pageStory.getContent(), pageStory.getTotalPages() - 1);
        }
        return StoryPagingResponse.newPageHasNext(pageStory.getContent(), pageStory.getTotalPages() - 1, pageStory.getPageable().getPageNumber() + 1);
    }

    private static StoryPagingResponse newPageHasNext(List<Story> stories, Integer lastPage, Integer nextPage) {
        return new StoryPagingResponse(getContents(stories), lastPage, nextPage);
    }

    private static StoryPagingResponse newLastScroll(List<Story> stories, Integer lastPage) {
        return newPageHasNext(stories, lastPage, LAST_PAGE);
    }

    private static List<FindAllStoryResponse> getContents(List<Story> stories){
        return stories.stream().map(story -> storyMapper.toFindAllStoryResponse(story)).toList();
    }


}
