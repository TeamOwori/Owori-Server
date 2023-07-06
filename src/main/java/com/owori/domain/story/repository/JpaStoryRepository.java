package com.owori.domain.story.repository;

import com.owori.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaStoryRepository extends JpaRepository<Story, Long>, StoryRepository {}
