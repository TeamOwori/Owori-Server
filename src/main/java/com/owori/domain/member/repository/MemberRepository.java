package com.owori.domain.member.repository;

import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Optional<Member> findById(UUID id);
    Optional<Member> findByClientIdAndAuthProvider(String clientId, AuthProvider authProvider);
    Member save(Member member);
    void updateRefreshToken(UUID id, String refreshToken);
    String findRefreshTokenById(UUID id);
}
