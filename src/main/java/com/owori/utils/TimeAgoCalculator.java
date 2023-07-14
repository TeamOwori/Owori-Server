package com.owori.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeAgoCalculator {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;

    /**
     * 방금 > 분 전 > 시간 전 > 일 전 > yy.MM.dd
     * @param localDateTime 계산할 날짜
     * @return 만들어진 문자열
     *
     */
    public static String timesAgo(LocalDateTime localDateTime) {
        long diffTime = localDateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        if (diffTime < SEC){
            return "방금";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }

        return localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));

    }

}
