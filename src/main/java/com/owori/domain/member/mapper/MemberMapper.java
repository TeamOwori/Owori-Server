package com.owori.domain.member.mapper;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.dto.request.MemberRequest;
import com.owori.domain.member.dto.response.MemberJwtResponse;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.entity.OAuth2Info;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemberMapper {
    public Member toEntity(String clientId, MemberRequest memberRequest) {
        return Member.builder()
                .oAuth2Info(new OAuth2Info(clientId, memberRequest.getAuthProvider()))
                .build();
    }

    public MemberJwtResponse toJwtResponse(JwtToken jwtToken, UUID memberId) {
        return MemberJwtResponse.builder()
                .jwtToken(jwtToken)
                .memberId(memberId)
                .build();
    }
}
