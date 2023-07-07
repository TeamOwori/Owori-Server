package com.owori.domain.image.repository;

import com.owori.domain.image.entity.Image;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository {
    Optional<Image> findById(UUID id);
    Image save(Image image);
}
