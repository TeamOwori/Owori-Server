package com.owori.domain.keyword.repository;

import com.owori.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaKeywordRepository extends JpaRepository<Keyword, UUID>, KeywordRepository {
}
