package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoryRepositoryCustom {
    Slice<Story> findAllStoryBySlice(Pageable pageable, Long lastId, Member member);

}