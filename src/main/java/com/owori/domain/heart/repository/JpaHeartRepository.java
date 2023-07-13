package com.owori.domain.heart.repository;

import com.owori.domain.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHeartRepository extends JpaRepository<Heart, UUID>, HeartRepository {
}
