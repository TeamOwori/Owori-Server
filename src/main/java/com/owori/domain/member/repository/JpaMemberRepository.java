package com.owori.domain.member.repository;

import com.owori.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMemberRepository extends JpaRepository<Member, UUID>, MemberRepository {}
