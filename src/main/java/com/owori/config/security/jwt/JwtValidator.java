package com.owori.config.security.jwt;

import com.owori.config.security.oauth.UserPrinciple;
import com.owori.config.security.oauth.UserPrincipleMapper;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final Key key;
    private final MemberRepository memberRepository;
    private final UserPrincipleMapper userPrincipleMapper;

    @Transactional
    public Authentication getAuthentication(String accessToken) {
        Claims claims = getTokenBodyClaims(accessToken);
        Optional<Member> member = memberRepository.findById(extractUUID(claims));
        if (member.isEmpty()) {
            return null;
        }
        UserPrinciple loginUser = userPrincipleMapper.mapToLoginUser(member.get());

        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    public UUID extractUUID(Claims claims) {
        return UUID.fromString(claims.get("id", String.class));
    }

    public Claims getTokenBodyClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
