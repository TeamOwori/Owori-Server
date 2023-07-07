package com.owori.domain.image.repository;

import com.owori.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface JpaImageRepository extends JpaRepository<Image, UUID>, ImageRepository {
}
