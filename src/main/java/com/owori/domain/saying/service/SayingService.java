package com.owori.domain.saying.service;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.exception.NoAuthorityUpdateException;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SayingService implements EntityLoader<Saying, UUID> {
    private final SayingRepository sayingRepository;
    private final MemberService memberService;
    private final SayingMapper sayingMapper;
    private final AuthService authService;

    public IdResponse<UUID> addSaying(AddSayingRequest request) {
        Member member = authService.getLoginUser();
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());
        Saying newSaying = sayingRepository.save(sayingMapper.toEntity(request.getContent(), member, tagMembers));
        return new IdResponse<>(newSaying.getId());
    }

    @Transactional
    public IdResponse<UUID> updateSaying(UUID sayingId, UpdateSayingRequest request) {
        Member member = authService.getLoginUser();
        Saying saying = loadEntity(sayingId);

        // 서로에게 한마디 작성자와 현재 유저가 동일하지 않을 경우 예외처리
        if(member != saying.getMember()) throw new NoAuthorityUpdateException();

        // tagMemberIds를 통해 tagMembers 구하기
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());

        // 새로운 정보로 업데이트
        saying.update(request.getContent(), tagMembers);

        return new IdResponse<>(saying.getId());
    }

    @Transactional
    public void deleteSaying(UUID sayingId) {
        Saying saying = loadEntity(sayingId);
        saying.delete();
    }

    public List<FindSayingByFamilyResponse> findSayingByFamily() {
        // BaseTime baseTime = new BaseTime();
        // Optional.ofNullable(baseTime.getUpdatedAt()).orElse(baseTime.getCreatedAt());
        return null; // todo: 로직 작성
    }

    @Override
    public Saying loadEntity(UUID uuid) {
        return sayingRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}

