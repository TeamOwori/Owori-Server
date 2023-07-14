package com.owori.domain.schedule.entity;



public enum Alarm {
    TODAY("당일"),
    A_DAY_AGO("전날"),
    A_WEEK_AGO("일주일전");

    private final String toKorean;

    private Alarm(String alarmType) {
        this.toKorean = alarmType;
    }

    public String getToKorean() {
        return toKorean;
    }
}
