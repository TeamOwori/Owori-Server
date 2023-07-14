package com.owori.domain.saying.exception;

public class NoAuthorityDeleteException extends IllegalArgumentException {
    private static final String MESSAGE = "삭제 권한이 없습니다.";

    public NoAuthorityDeleteException() { super(MESSAGE); }
}
