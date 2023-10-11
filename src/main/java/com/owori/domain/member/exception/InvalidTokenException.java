package com.owori.domain.member.exception;

public class InvalidTokenException  extends IllegalArgumentException {
    private static final String MESSAGE = "유효하지 않은 ID 토큰입니다";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
