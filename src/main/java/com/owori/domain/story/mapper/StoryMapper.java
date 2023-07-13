package com.owori.domain.story.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.request.AddStoryRequest;
import com.owori.domain.story.dto.response.FindAllStoryResponse;
import com.owori.domain.story.entity.Story;
import org.springframework.stereotype.Component;


@Component
public class StoryMapper {
    public Story toEntity(AddStoryRequest request, Member member){
        return Story.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .member(member)
                .build();
    }

    public FindAllStoryResponse of(Story story){
        return FindAllStoryResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .contents(story.getContents())
                .image(story.getImages() == null || story.getImages().isEmpty() ? null : story.getImages().get(0).getUrl())
                .heartCnt(story.getHearts().size())
                .commentCnt(story.getComments().size())
                .writer(story.getMember().getNickname())
                .startDate(story.getStartDate())
                .endDate(story.getEndDate())
                .build();
    }
}


