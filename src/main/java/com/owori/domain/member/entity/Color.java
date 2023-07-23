package com.owori.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Color {
    RED("빨간색", 1),
    YELLOW("노란색", 2),
    GREEN("초록색", 3),
    PINK("분홍색", 4),
    SKYBLUE("하늘색", 5),
    BLUE("파란색", 6),
    PURPLE("보라색", 7);

    private final String toKorean;
    private final int num;

    public static Color getNextColor(List<Color> colors) {
        return Arrays.stream(Color.values())
                .filter(c -> !colors.contains(c))
                .findFirst()
                .orElseGet(() ->Arrays.stream(Color.values()).filter(c -> c.getNum() == colors.size() % 7 + 1).findFirst().orElseThrow());
    }
}
