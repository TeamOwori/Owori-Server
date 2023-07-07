package com.owori.domain.story.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.dto.request.AddStoryRequest;
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
}


