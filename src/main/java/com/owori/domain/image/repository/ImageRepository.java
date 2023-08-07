package com.owori.domain.image.repository;

import com.owori.domain.image.entity.Image;
import com.owori.domain.story.entity.Story;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository {
    Optional<Image> findById(UUID id);
    Image save(Image image);
    List<Image> findAllByStory(Story story);

    List<Image> findAllByUrls(List<String> urls);

}
