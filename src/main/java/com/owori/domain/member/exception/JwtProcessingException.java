package com.owori.domain.member.exception;

public class JwtProcessingException extends IllegalArgumentException {
    private static final String MESSAGE = "Jwt 토큰 검증에 실패했습니다.";

    public JwtProcessingException() {
        super(MESSAGE);
    }
}
