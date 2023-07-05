package com.owori.domain.member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Info {
    @Column(unique = true, nullable = false)
    private String accountId;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
}
