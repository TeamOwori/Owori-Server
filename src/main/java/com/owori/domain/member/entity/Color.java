package com.owori.domain.member.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Color {
    RED("빨간색"), ORANGE("주황색"), YELLOW("노란색"), GREEN("초록색"), BLUE("파란색"); // todo 색상 목록 확인 필요

    private final String toKorean;
}
