package com.owori.domain.heart.mapper;

import com.owori.domain.heart.entity.Heart;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.stereotype.Component;

@Component
public class HeartMapper {
    public Heart toEntity(Member member, Story story) {
        return Heart.builder()
                .story(story)
                .member(member)
                .build();
    }
}
