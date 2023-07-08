package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface StoryRepositoryCustom {
    Slice<Story> findAllStoryByCreateAt(Pageable pageable, Long lastId, Member member);
    Slice<Story> findAllStoryByEventAt(Pageable pageable, LocalDate startDate, Member member);

}
