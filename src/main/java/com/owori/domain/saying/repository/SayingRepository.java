package com.owori.domain.saying.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.saying.entity.Saying;

import java.util.Optional;
import java.util.UUID;

public interface SayingRepository {

    Optional<Saying> findById(UUID uuid);
    Saying save(Saying saying);
    Optional<Saying> findByMemberAndStatus(Member member, Boolean status);

}
