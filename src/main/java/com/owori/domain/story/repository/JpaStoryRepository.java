package com.owori.domain.story.repository;

import com.owori.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaStoryRepository extends JpaRepository<Story, UUID>, StoryRepository {}
