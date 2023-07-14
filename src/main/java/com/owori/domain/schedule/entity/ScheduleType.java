package com.owori.domain.schedule.entity;


public enum ScheduleType {

    FAMILY("가족"),
    INDIVIDUAL("개인");

    private final String toKorean;

    ScheduleType(String scheduleType) {
        this.toKorean = scheduleType;
    }

    public String getToKorean() {
        return toKorean;
    }
}
