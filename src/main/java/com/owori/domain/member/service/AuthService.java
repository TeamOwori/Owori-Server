package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import com.owori.config.security.jwt.JwtTokenProvider;
import com.owori.config.security.jwt.JwtValidator;
import com.owori.config.security.oauth.UserPrinciple;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.exception.JwtProcessingException;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidator jwtValidator;
    public JwtToken refreshToken(final String oldRefreshToken, final String oldAccessToken) {
        validateTokens(oldRefreshToken, oldAccessToken);

        UserPrinciple user = getUserDetails(oldAccessToken);

        validateSavedRefreshTokenIfExpired(oldRefreshToken, UUID.fromString(user.getName()));

        return findMemberAndUpdateRefreshToken(user);
    }

    private JwtToken findMemberAndUpdateRefreshToken(final UserPrinciple user) {
        JwtToken jwtToken = jwtTokenProvider.createToken(user);

        memberRepository.updateRefreshToken(UUID.fromString(user.getName()), jwtToken.getRefreshToken());
        return jwtToken;
    }

    private UserPrinciple getUserDetails(final String oldAccessToken) {
        Authentication authentication = jwtValidator.getAuthentication(oldAccessToken);
        return (UserPrinciple) authentication.getPrincipal();
    }

    private void validateTokens(final String oldRefreshToken, final String oldAccessToken) {
        validateJwtTokens(oldRefreshToken, oldAccessToken);
        validateIfRefreshTokenExpired(oldRefreshToken);
    }

    private void validateSavedRefreshTokenIfExpired(final String oldRefreshToken, final UUID id) {
        String savedToken = memberRepository.findRefreshTokenById(id);
        if (!savedToken.equals(oldRefreshToken)) {
            throw new JwtProcessingException();
        }
    }

    private void validateIfRefreshTokenExpired(final String oldRefreshToken) {
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtProcessingException();
        }
    }

    private void validateJwtTokens(final String oldRefreshToken, final String oldAccessToken) {
        if (!StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken)) {
            throw new JwtProcessingException();
        }
    }

    public UUID getLoginUserId() {
        return UUID.fromString(((UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
    }

    public Member getLoginUser() {
        return memberRepository.findById(getLoginUserId())
                .orElseThrow(EntityNotFoundException::new);
    }
}
