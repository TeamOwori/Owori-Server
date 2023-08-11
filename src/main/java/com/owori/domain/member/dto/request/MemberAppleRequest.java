package com.owori.domain.member.dto.request;

import com.owori.domain.member.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAppleRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String authorizationCode;
    @NotNull
    private AuthProvider authProvider;
}
