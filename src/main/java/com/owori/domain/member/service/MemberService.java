package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.client.KakaoMemberClient;
import com.owori.domain.member.dto.client.KakaoMemberResponse;
import com.owori.domain.member.dto.request.EmotionalBadgeRequest;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberProfileRequest;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.exception.NoSuchProfileImageException;
import com.owori.domain.member.mapper.MemberMapper;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.global.dto.ImageResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import com.owori.utils.S3ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final S3ImageComponent s3ImageComponent;
    private final KakaoMemberClient kakaoMemberClient;

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberJwtResponse saveIfNone(final MemberRequest memberRequest) {
        String clientId = Long.toString(requestToKakao(memberRequest.getToken()).getId());

        Member member = memberRepository.findByClientIdAndAuthProvider(clientId, memberRequest.getAuthProvider())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(clientId, memberRequest)));

        JwtToken jwtToken = createMemberJwtToken(member, member.getOAuth2Info().getClientId());
        return memberMapper.toJwtResponse(jwtToken, member.getId());
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
    public List<Member> findMembersByIds(List<UUID> memberIds) {
        return memberRepository.findAllByIdIn(memberIds);
    }

    @Transactional
    public void updateEmotionalBadge(EmotionalBadgeRequest emotionalBadgeRequest) {
        authService.getLoginUser().updateEmotionalBadge(emotionalBadgeRequest.getEmotionalBadge());
    }
}
