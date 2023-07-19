package com.owori.domain.keyword.repository;

import com.owori.domain.keyword.entity.Keyword;
import com.owori.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeywordRepository {
    Optional<Keyword> findByContents(String contents);
    List<Keyword> findByMember(Member member);
    Keyword save(Keyword keyword);
    void deleteAll();
    void delete(Keyword keyword);
    Optional<Keyword> findById(UUID id);
}
