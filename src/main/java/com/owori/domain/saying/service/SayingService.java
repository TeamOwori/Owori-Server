package com.owori.domain.saying.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.entity.SayingTagMember;
import com.owori.domain.saying.exception.NoAuthorityUpdateException;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.domain.schedule.dto.response.FindScheduleByMonthResponse;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // 이전 서로에게 한마디 삭제하기
        Saying oldSaying = sayingRepository.findByMemberAndStatus(member, true);
        oldSaying.delete();

        // 새로운 서로에게 한마디 생성하기
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
        Member nowMember = authService.getLoginUser();
        Family family = nowMember.getFamily();

        List<Saying> sayingList = family.getMembers().stream()
                .map(member -> sayingRepository.findByMemberAndStatus(member, true))
                .toList();

        /*
        List<Saying> sayingList = family.getMembers().stream()
                .map(Member::getSaying)
                .toList();
        */
        return sayingList.stream()
                .map(saying -> new FindSayingByFamilyResponse(saying.getId(), saying.getContent(), saying.getMember().getId(),
                        saying.getTagMembers().stream().map(SayingTagMember::getMember).map(Member::getId).toList(),
                        Optional.ofNullable(saying.getBaseTime().getUpdatedAt()).orElse(saying.getBaseTime().getCreatedAt())))
                .toList();
    }

    @Override
    public Saying loadEntity(UUID uuid) {
        return sayingRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}

