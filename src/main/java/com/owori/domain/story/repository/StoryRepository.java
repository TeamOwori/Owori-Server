package com.owori.domain.story.repository;

import com.owori.domain.story.entity.Story;

import java.util.Optional;

public interface StoryRepository {
    Story save(Story story);
    Optional<Story> findById(Long id);
}
