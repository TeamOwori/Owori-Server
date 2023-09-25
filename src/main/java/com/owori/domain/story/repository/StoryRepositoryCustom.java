package com.owori.domain.story.repository;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryRepositoryCustom {
    Page<Story> findStoryBySearch(Pageable pageable, String keyword, Family family);
    Page<Story> findStoryByWriter(Pageable pageable, Member member);
    Page<Story> findStoryByHeart(Pageable pageable, Member member);
    List<Story> findStoryBySearch2(String keyword, Family family);
    Page<Story> findAllStory(Pageable pageable, Family family);
}
