package com.owori.domain.story.repository;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface StoryRepositoryCustom {
    Slice<Story> findAllStory(Pageable pageable, Family family, LocalDate date);
    Slice<Story> findStoryBySearch(Pageable pageable, String keyword, Family family, LocalDate date);
    Slice<Story> findStoryByWriter(Pageable pageable, Member member, LocalDate date);
    Slice<Story> findStoryByHeart(Pageable pageable, Member member, LocalDate date);
}
