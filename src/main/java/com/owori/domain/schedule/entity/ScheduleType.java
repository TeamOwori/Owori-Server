package com.owori.domain.schedule.entity;


import lombok.Getter;

@Getter
public enum ScheduleType {

    FAMILY("가족"),
    INDIVIDUAL("개인");

    private final String toKorean;

    ScheduleType(String scheduleType) {
        this.toKorean = scheduleType;
    }
}
