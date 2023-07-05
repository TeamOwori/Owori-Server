package com.owori.domain.member.service;

import com.owori.config.security.oauth.OAuth2Request;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
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
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member saveIfNone(final OAuth2Request oAuth2Request) {
        String accountId = oAuth2Request.getAccountId();
        return memberRepository.findByAccountId(accountId)
                .orElseGet(() -> memberRepository.save(setUp(oAuth2Request)));
    }

    private Member setUp(final OAuth2Request oAuth2Request) {
        OAuth2Info oAuth2Info = new OAuth2Info(oAuth2Request.getAccountId(), oAuth2Request.getAuthProvider());
        return new Member(oAuth2Request.getName(), oAuth2Info);
    }
}
