package com.owori.domain.heart.repository;

import com.owori.domain.heart.entity.Heart;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;

import java.util.Optional;
import java.util.UUID;

public interface HeartRepository {
    Optional<Heart> findById(UUID id);
    Heart save(Heart heart);
    Optional<Heart> findByMemberAndStory(Member member, Story story);
    boolean existsByMemberAndStory(Member member, Story story);
    Long countByMember(Member member);
}
