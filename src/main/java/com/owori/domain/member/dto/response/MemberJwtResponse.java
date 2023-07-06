package com.owori.domain.member.dto.response;

import com.owori.config.security.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MemberJwtResponse {
    private UUID memberId;
    private JwtToken jwtToken;
}
