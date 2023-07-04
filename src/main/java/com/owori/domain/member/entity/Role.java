package com.owori.domain.member.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("관리자"), ROLE_USER("유저");

    private final String toKorean;
}
