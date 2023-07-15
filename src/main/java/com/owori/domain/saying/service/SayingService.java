package com.owori.domain.saying.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.exception.NoAuthorityException;
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

    @Transactional
    public IdResponse<UUID> addSaying(AddSayingRequest request) {
        Member member = authService.getLoginUser();
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());

        // 이전 서로에게 한마디 삭제하기
        Optional<Saying> oldSaying = sayingRepository.findByMemberAndModifiable(member, true);
        oldSaying.ifPresent(Saying::changeModifiable);

        // 새로운 서로에게 한마디 생성하기
        Saying newSaying = sayingRepository.save(sayingMapper.toEntity(request.getContent(), member, tagMembers));
        return new IdResponse<>(newSaying.getId());
    }

    @Transactional
    public IdResponse<UUID> updateSaying(UUID sayingId, UpdateSayingRequest request) {
        Member member = authService.getLoginUser();
        Saying saying = loadEntity(sayingId);

        // 서로에게 한마디 작성자와 현재 유저가 동일하지 않을 경우 예외처리
        if(!member.equals(saying.getMember())) throw new NoAuthorityException("수정");

        // tagMemberIds 를 통해 tagMembers 구하기
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());

        // 새로운 정보로 업데이트
        saying.update(request.getContent(), tagMembers);

        return new IdResponse<>(saying.getId());
    }

    @Transactional
    public void deleteSaying(UUID sayingId) {
        Saying saying = loadEntity(sayingId);
        if(!saying.getMember().equals(authService.getLoginUser())) throw new NoAuthorityException("삭제");
        saying.changeModifiable();
    }

    public List<FindSayingByFamilyResponse> findSayingByFamily() {
        Member nowMember = authService.getLoginUser();
        Family family = nowMember.getFamily();

        List<Saying> sayingList = family.getMembers().stream()
                .map(member -> sayingRepository.findByMemberAndModifiable(member, true))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return sayingMapper.toResponseList(sayingList);
    }

    @Override
    public Saying loadEntity(UUID uuid) {
        return sayingRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }
}

