package com.owori.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.client.ApplePublicKeyClient;
import com.owori.domain.member.client.KakaoMemberClient;
import com.owori.domain.member.dto.client.ApplePublicKeyResponse;
import com.owori.domain.member.dto.client.KakaoMemberResponse;
import com.owori.domain.member.dto.collection.AppleMap;
import com.owori.domain.member.dto.request.*;
import com.owori.domain.member.dto.response.*;
import com.owori.domain.member.entity.AuthProvider;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.exception.NoSuchProfileImageException;
import com.owori.domain.member.jwt.AppleKeyGenerator;
import com.owori.domain.member.jwt.JwtParser;
import com.owori.domain.member.mapper.MemberMapper;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.saying.dto.response.SayingByFamilyResponse;
import com.owori.domain.saying.mapper.SayingMapper;
import com.owori.domain.schedule.dto.response.ScheduleDDayResponse;
import com.owori.domain.schedule.service.ScheduleService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.service.FacadeService;
import com.owori.domain.story.service.StoryService;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.PublicKey;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final SayingMapper sayingMapper;
    private final StoryService storyService;
    private final ScheduleService scheduleService;
    private final S3ImageComponent s3ImageComponent;
    private final KakaoMemberClient kakaoMemberClient;
    private final ApplePublicKeyClient applePublicKeyClient;
    private final JwtParser jwtParser;
    private final AppleKeyGenerator appleKeyGenerator;
    private final FacadeService facadeService;

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberJwtResponse saveWithKakaoIfNone(final MemberKakaoRequest memberKakaoRequest) {
        String clientId = getKakaoClientId(memberKakaoRequest);

        Optional<Member> member = memberRepository.findByClientIdAndAuthProvider(clientId, AuthProvider.KAKAO);
        if (member.isPresent()) {
            return getServiceMemberJwtResponse(member.get());
        }
        return getNewMemberJwtResponse(AuthProvider.KAKAO, clientId);
    }

    public MemberJwtResponse saveWithAppleIfNone(final MemberAppleRequest memberAppleRequest) throws JsonProcessingException {
        String clientId = getAppleClientId(memberAppleRequest);

        Optional<Member> member = memberRepository.findByClientIdAndAuthProvider(clientId, AuthProvider.APPLE);
        if (member.isPresent()) {
            return getServiceMemberJwtResponse(member.get());
        }
        return getNewMemberJwtResponse(AuthProvider.APPLE, clientId);
    }

    private String getKakaoClientId(final MemberKakaoRequest memberKakaoRequest) {
        return Long.toString(requestToKakao(memberKakaoRequest.getToken()).getId());
    }

    private String getAppleClientId(final MemberAppleRequest memberAppleRequest) throws JsonProcessingException {
        String identityToken = memberAppleRequest.getToken();
        AppleMap appleMap = jwtParser.parseHeader(identityToken);
        ApplePublicKeyResponse response = applePublicKeyClient.requestToApple();
        PublicKey publicKey = appleKeyGenerator.generatePublicKey(appleMap, response);
        return jwtParser.parseClaims(identityToken, publicKey);
    }

    private MemberJwtResponse getNewMemberJwtResponse(final AuthProvider authProvider, final String clientId) {
        Member member = memberRepository.save(memberMapper.toEntity(clientId, authProvider));
        JwtToken jwtToken = createMemberJwtToken(member);
        return memberMapper.toJwtResponse(jwtToken, member.getId(), member.isServiceMember());
    }

    private MemberJwtResponse getServiceMemberJwtResponse(final Member member) {
        JwtToken jwtToken = createMemberJwtToken(member);
        return memberMapper.toJwtResponse(jwtToken, member.getId(), member.isServiceMember());
    }

    private JwtToken createMemberJwtToken(final Member member) {
        return authService.createAndUpdateToken(member);
    }

    private KakaoMemberResponse requestToKakao(final String token) {
        return kakaoMemberClient.requestToKakao(token);
    }

    @Transactional
    public MemberValidateResponse updateMemberDetails(final MemberDetailsRequest memberDetailsRequest) {
        authService.getLoginUser().update(
                memberDetailsRequest.getNickname(),
                memberDetailsRequest.getBirthday());
        return new MemberValidateResponse(Boolean.TRUE);
    }

    @Transactional
    public MemberImageResponse updateMemberProfileImage(final MultipartFile profileImage) {
        String profileImageUrl = uploadImage(profileImage);
        authService.getLoginUser().updateProfileImage(profileImageUrl);
        return new MemberImageResponse(profileImageUrl);
    }

    private String uploadImage(final MultipartFile profileImage) {
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
        // 멤버 삭제 시 작성한 스토리 작성자 null 처리
        Member nowMember = authService.getLoginUser();
        List<Story> stories = storyService.findStoriesByWriter(nowMember).stream().toList();
        stories.forEach(Story::deleteWriter);

        authService.getLoginUser().delete();
    }

    /**
     * 멤버 아이디 리스트를 통해 멤버 리스트를 반환해주는 함수
     *
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

    @Transactional(readOnly = true)
    public MemberHomeResponse findHomeData() {
        Member nowMember = authService.getLoginUser();
        List<ScheduleDDayResponse> dDayByFamilyResponses = scheduleService.findDDayByFamily();
        List<SayingByFamilyResponse> sayingResponses = nowMember.getFamily().getMembers().stream()
                .map(Member::getSaying).filter(Objects::nonNull).map(sayingMapper::toResponse).toList();
        return memberMapper.toHomeResponse(nowMember, dDayByFamilyResponses, sayingResponses);
    }

    @Transactional(readOnly = true)
    public MyPageProfileResponse getMyPageProfile() {
        Member loginUser = authService.getLoginUser();
        return memberMapper.toMyPageProfileResponse(loginUser, facadeService.findStoryNumByMember(loginUser), facadeService.findHeartNumByMember(loginUser));
    }


    @Transactional(readOnly = true)
    public MemberColorResponse getEnableColor() {
        Member loginUser = authService.getLoginUser();
        Set<Member> familyMembers = loginUser.getFamily().getMembers();
        familyMembers.removeIf(loginUser::equals);
        return memberMapper.toColorResponse(familyMembers);
    }
}
