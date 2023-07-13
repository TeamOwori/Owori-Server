package com.owori.domain.member.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmotionalBadge {
    JOY("기뻐요"),
    HAPPY("행복쓰"),
    SO_HAPPY("엄청 행복쓰"),
    LOVE("사랑해"),
    SURRISED("놀람"),
    INSIDIOUS("음흉쓰"),
    NORMAL("보통"),
    SLEEPY("졸림"),
    FAINT("깨꼬닥"),
    SULKY("삐졌어"),
    SAD("슬퍼요"),
    CRY("엉엉"),
    GOOSE_BUMPS("소오오름"),
    ANGRY("화남"),
    VERY_ANGRY("매우 화남"),
    NONE("선택 안함");

    private final String toKorean;
}
