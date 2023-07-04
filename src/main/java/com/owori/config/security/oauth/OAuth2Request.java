package com.owori.config.security.oauth;

import com.owori.domain.member.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2Request {
    private String accountId;
    private String name;
    private AuthProvider authProvider;
}
