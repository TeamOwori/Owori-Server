package com.owori.domain.family.repository;

import com.owori.domain.family.entity.Family;

import java.util.Optional;
import java.util.UUID;

public interface FamilyRepository {
    Optional<Family> findById(UUID id);
}
