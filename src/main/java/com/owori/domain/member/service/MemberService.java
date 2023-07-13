package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.client.KakaoMemberClient;
import com.owori.domain.member.dto.client.KakaoMemberResponse;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberProfileRequest;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.AuthProvider;
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
}
