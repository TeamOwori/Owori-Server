package com.owori.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplePublicKeyDetails {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;
}
