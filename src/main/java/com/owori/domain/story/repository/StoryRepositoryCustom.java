package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface StoryRepositoryCustom {
    Slice<Story> findAllAlbumStoryByCreatedAt(Pageable pageable, LocalDate lastId, Member member);
    Slice<Story> findAllAlbumStoryByEventAt(Pageable pageable, LocalDate startDate, Member member);
    Slice<Story> findAllListStoryByCreatedAt(Pageable pageable, Member member, LocalDate startDate);
    Slice<Story> findAllListStoryByEventAt(Pageable pageable, Member member, LocalDate startDate);
}
