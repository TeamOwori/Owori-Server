package com.owori.domain.comment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.IntPredicate;

@Getter
@RequiredArgsConstructor
public enum TimesAgo {
    SECONDS(time -> time < 60, "초 전", ChronoUnit.SECONDS),
    MINUTE(time -> time >= 60 && time < 3600, "분 전", ChronoUnit.MINUTES),
    HOUR(time -> time >= 3600 && time < 86400, "시간 전", ChronoUnit.HOURS),
    DAY(time -> time >= 86400 && time < 2592000, "일 전", ChronoUnit.DAYS);

    private final IntPredicate timeToSecond;
    private final String message;
    private final ChronoUnit chronoUnit;

    public static String of(LocalDateTime localDateTime) {
        int diffTime = (int) localDateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        return Arrays.stream(TimesAgo.values())
                .filter(timesAgo -> timesAgo.timeToSecond.test(diffTime))
                .map(timesAgo -> {
                    long value = diffTime / timesAgo.getChronoUnit().getDuration().getSeconds();
                    return value + timesAgo.getMessage();
                })
                .findFirst()
                .orElseGet(() -> localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd")));

    }
}




