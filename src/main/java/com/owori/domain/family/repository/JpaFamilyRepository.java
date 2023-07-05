package com.owori.domain.family.repository;

import com.owori.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFamilyRepository extends JpaRepository<Family, UUID>, FamilyRepository {}
