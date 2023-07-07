package com.owori.config.security.jwt;

import com.owori.domain.member.entity.Member;
import com.owori.domain.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Key key;
    private final MemberRepository memberRepository;
    private static final Long ACCESS_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 1000; // 1 Day
    private static final Long REFRESH_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 14 * 1000; // 14 Days

    public JwtToken createToken(Member loginUser, String token) {
        Claims claims = getClaims(loginUser, token);

        String accessToken = getToken(loginUser, claims, ACCESS_TOKEN_EXPIRE_LENGTH);
        String refreshToken = getToken(loginUser, claims, REFRESH_TOKEN_EXPIRE_LENGTH);

        saveRefreshToken(refreshToken, loginUser);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | IllegalStateException e) {
            return false;
        }
    }

    private void saveRefreshToken(String refreshToken, Member loginUser) {
        UUID id = loginUser.getId();
        memberRepository.updateRefreshToken(id, refreshToken);
    }

    private Claims getClaims(Member loginUser, String token) {
        Claims claims = Jwts.claims();
        claims.put("id", loginUser.getId().toString());
        claims.put("token", token);
        return claims;
    }

    private String getToken(Member loginUser, Claims claims, Long validationSecond) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(loginUser.getId().toString())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + validationSecond))
                .compact();
    }
}
