package com.owori.domain.saying.service;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.member.service.MemberService;
import com.owori.domain.saying.dto.request.AddSayingRequest;
import com.owori.domain.saying.dto.request.UpdateSayingRequest;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.saying.dto.response.SayingIdResponse;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.domain.saying.repository.SayingRepository;
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
    public SayingIdResponse addSaying(AddSayingRequest request) {
        Member member = authService.getLoginUser();
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());

        // 이전 서로에게 한마디 삭제하기
        Optional<Saying> oldSaying = sayingRepository.findByMemberAndModifiable(member, Boolean.TRUE);
        oldSaying.ifPresent(Saying::changeModifiable);

        // 새로운 서로에게 한마디 생성하기
        Saying newSaying = sayingRepository.save(sayingMapper.toEntity(request.getContent(), member, tagMembers));
        return new SayingIdResponse(newSaying.getId());
    }

    @Transactional
    public SayingIdResponse updateSaying(UpdateSayingRequest request) {
        Saying saying = loadEntity(request.getSayingId());

        // 서로에게 한마디 작성자와 현재 유저가 동일하지 않을 경우 예외처리
        if(!authService.getLoginUser().getId().equals(saying.getMember().getId())) throw new NoAuthorityException();

        // tagMemberIds 를 통해 tagMembers 구하기
        List<Member> tagMembers = memberService.findMembersByIds(request.getTagMembersId());

        // 새로운 정보로 업데이트
        saying.update(request.getContent(), tagMembers);

        return new SayingIdResponse(saying.getId());
    }

    @Transactional
    public void deleteSaying(UUID sayingId) {
        Saying saying = loadEntity(sayingId);
        if(!saying.getMember().getId().equals(authService.getLoginUser().getId())) throw new NoAuthorityException();
        saying.changeModifiable();
    }

    public List<SayingByFamilyResponse> findSayingByFamily() {
        Family family = authService.getLoginUser().getFamily();

        List<Saying> sayingList = family.getMembers().stream()
                .map(member -> sayingRepository.findByMemberAndModifiable(member, Boolean.TRUE))
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

