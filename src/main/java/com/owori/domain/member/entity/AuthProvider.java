package com.owori.domain.member.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthProvider {
    APPLE("애플"), KAKAO("카카오");

    private final String toKorean;
}
