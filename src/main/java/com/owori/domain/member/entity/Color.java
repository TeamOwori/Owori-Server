package com.owori.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Color {
    RED("빨간색", 0),
    YELLOW("노란색", 1),
    GREEN("초록색", 2),
    PINK("분홍색", 3),
    SKYBLUE("하늘색", 4),
    BLUE("파란색", 5),
    PURPLE("보라색", 6);

    private final String toKorean;
    private final int num;

    public static Color getNextColor(List<Color> colors) {
        return Arrays.stream(Color.values())
                .filter(c -> !colors.contains(c))
                .findFirst()
                .orElse(Arrays.stream(Color.values()).filter(c -> c.getNum() == colors.size() / 7 + 1).findFirst().orElseThrow());
    }
}
