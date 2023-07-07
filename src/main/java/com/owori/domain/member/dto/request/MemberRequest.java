package com.owori.domain.member.dto.request;

import com.owori.domain.member.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String token;
    private AuthProvider authProvider;
}
