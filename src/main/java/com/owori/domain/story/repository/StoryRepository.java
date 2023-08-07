package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {
    Story save(Story story);
    Optional<Story> findById(UUID id);
    Long countByMember(Member member);
}
