package com.owori.domain.member.repository;

import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface JpaMemberRepository extends JpaRepository<Member, UUID>, MemberRepository {

    @Query("SELECT m FROM Member m WHERE m.oAuth2Info.clientId = :clientId AND m.oAuth2Info.authProvider = :authProvider")
    Optional<Member> findByClientIdAndAuthProvider(String clientId, AuthProvider authProvider);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.refreshToken = :refreshToken WHERE m.id = :id")
    void updateRefreshToken(UUID id, String refreshToken);

    @Query("SELECT m.refreshToken FROM Member m WHERE m.id = :id")
    String findRefreshTokenById(UUID id);
}
