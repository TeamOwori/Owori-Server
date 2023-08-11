package com.owori.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAppleRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String authorizationCode;
}
