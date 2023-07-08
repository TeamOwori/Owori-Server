package com.owori.domain.story.repository;

import com.owori.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {
    Story save(Story story);
    Optional<Story> findById(Long id);
}
