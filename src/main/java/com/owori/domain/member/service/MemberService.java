package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.family.entity.Family;
import com.owori.domain.member.client.KakaoMemberClient;
import com.owori.domain.member.dto.client.KakaoMemberResponse;
import com.owori.domain.member.dto.request.EmotionalBadgeRequest;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberProfileRequest;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.FindHomeResponse;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.dto.response.MemberProfileResponse;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.exception.NoSuchProfileImageException;
import com.owori.domain.member.mapper.MemberMapper;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.saying.dto.response.FindSayingByFamilyResponse;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.global.dto.ImageResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final SayingMapper sayingMapper;
    private final S3ImageComponent s3ImageComponent;
    private final KakaoMemberClient kakaoMemberClient;

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberJwtResponse saveIfNone(final MemberRequest memberRequest) {
        String clientId = getClientId(memberRequest);

        Member member = memberRepository.findByClientIdAndAuthProvider(clientId, memberRequest.getAuthProvider())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(clientId, memberRequest)));

        JwtToken jwtToken = createMemberJwtToken(member, member.getOAuth2Info().getClientId());
        return memberMapper.toJwtResponse(jwtToken, member.getId());
    }

    private String getClientId(final MemberRequest memberRequest) {
        if (memberRequest.getAuthProvider().equals(AuthProvider.KAKAO)) {
            return Long.toString(requestToKakao(memberRequest.getToken()).getId());
        }
        return memberRequest.getToken();
    }

    private JwtToken createMemberJwtToken(final Member member, final String token) {
        return authService.createAndUpdateToken(member, token);
    }

    private KakaoMemberResponse requestToKakao(final String token) {
        return kakaoMemberClient.requestToKakao(token);
    }

    @Transactional
    public void updateMemberDetails(final MemberDetailsRequest memberDetailsRequest) {
        authService.getLoginUser().update(
                memberDetailsRequest.getNickname(),
                memberDetailsRequest.getBirthDay());
    }

    @Transactional
    public ImageResponse updateMemberProfileImage(final MultipartFile profileImage) throws IOException {
        String profileImageUrl = uploadImage(profileImage);
        authService.getLoginUser().updateProfileImage(profileImageUrl);
        return new ImageResponse(profileImageUrl);
    }

    private String uploadImage(final MultipartFile profileImage) throws IOException {
        if (profileImage.isEmpty()) {
            throw new NoSuchProfileImageException();
        }
        return s3ImageComponent.uploadImage("profile-image", profileImage);
    }

    @Transactional
    public void updateMemberProfile(final MemberProfileRequest memberProfileRequest) {
        Member member = authService.getLoginUser();
        member.updateProfile(
                memberProfileRequest.getNickname(),
                memberProfileRequest.getBirthday(),
                memberProfileRequest.getColor());
    }

    @Transactional
    public void deleteMember() {
        authService.getLoginUser().delete();
    }

    /**
     * 멤버 아이디 리스트를 통해 멤버 리스트를 반환해주는 함수
     * @param memberIds 멤버 아이디 리스트
     * @return 멤버 리스트
     */
    public List<Member> findMembersByIds(final List<UUID> memberIds) {
        return memberRepository.findAllByIdIn(memberIds);
    }

    @Transactional
    public void updateEmotionalBadge(final EmotionalBadgeRequest emotionalBadgeRequest) {
        authService.getLoginUser().updateEmotionalBadge(emotionalBadgeRequest.getEmotionalBadge());
    }

    public FindHomeResponse findHomeData() {
        // 현재 로그인 유저 정보 받아오기
        Member nowMember = authService.getLoginUser();
        Family family = nowMember.getFamily();

        // 현재 유저를 제외한 가족 정보 받기
        Set<Member> familyAllMembers = family.getMembers();
        List<FindSayingByFamilyResponse> sayingsData = familyAllMembers.stream()
                        .map(Member::getSaying).map(sayingMapper::toResponse).toList();

        familyAllMembers.removeIf(member -> member.equals(nowMember));

        List<Member> familyMembers = familyAllMembers.stream()
                .sorted(Comparator.comparing(Member::getNickname))
                .toList();

        // 각 유저의 프로필 정보 받기
        // 본인이 맨 앞으로 정렬
        // 나머지는 닉네임순으로 정렬
        MemberProfileResponse membersProfileData = new MemberProfileResponse(nowMember.getId(), nowMember.getNickname(), nowMember.getProfileImage(), nowMember.getEmotionalBadge());
        List<MemberProfileResponse> memberProfileResponses = familyMembers.stream().map(member -> new MemberProfileResponse(member.getId(), member.getNickname(), member.getProfileImage(), member.getEmotionalBadge())).collect(Collectors.toList());
        memberProfileResponses.add(0, membersProfileData);

        return new FindHomeResponse(family.getFamilyGroupName(),memberProfileResponses, family.getImages(), sayingsData);
    }
}
