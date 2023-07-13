package com.owori.domain.saying.exception;

public class NoAuthorityUpdateException extends IllegalArgumentException {
    private static final String MESSAGE = "수정 권한이 없습니다.";

    public NoAuthorityUpdateException() {
        super(MESSAGE);
    }
}
