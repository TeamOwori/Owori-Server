package com.owori.domain.schedule.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {

    FAMILY("가족"),
    INDIVIDUAL("개인");

    private final String toKorean;
}
