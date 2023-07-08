package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.dto.request.MemberDetailsRequest;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.exception.NoSuchProfileImageException;
import com.owori.domain.member.mapper.MemberMapper;
import com.owori.domain.member.repository.MemberRepository;
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

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberJwtResponse saveIfNone(final MemberRequest memberRequest) {
        Member member = memberRepository.findByTokenAndAuthProvider(memberRequest.getToken(), memberRequest.getAuthProvider())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(memberRequest)));

        JwtToken jwtToken = createMemberJwtToken(member, member.getOAuth2Info().getToken());
        return memberMapper.toJwtResponse(jwtToken, member.getId());
    }

    private JwtToken createMemberJwtToken(final Member member, final String token) {
        return authService.createAndUpdateToken(member, token);
    }

    @Transactional
    public void updateMemberDetails(final MemberDetailsRequest memberDetailsRequest) {
        authService.getLoginUser().update(
                memberDetailsRequest.getNickname(),
                memberDetailsRequest.getBirthDay());
    }

    @Transactional
    public void updateMemberProfileImage(final MultipartFile profileImage) throws IOException {
        String profileImageUrl = uploadImage(profileImage);
        authService.getLoginUser().updateProfileImage(profileImageUrl);
    }

    private String uploadImage(final MultipartFile profileImage) throws IOException {
        if (profileImage.isEmpty()) {
            throw new NoSuchProfileImageException();
        }
        return s3ImageComponent.uploadImage("profile-image", profileImage);
    }
}
