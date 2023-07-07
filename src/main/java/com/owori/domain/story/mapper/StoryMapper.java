package com.owori.domain.story.mapper;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class StoryMapper {
    public Story toEntity(String title, String contents, LocalDate startDate, LocalDate endDate, Member member){
        return Story.builder()
                .title(title)
                .contents(contents)
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .build();
    }
}


