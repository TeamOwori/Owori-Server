package com.owori.domain.member.service;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;

    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
