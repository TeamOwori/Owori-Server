package com.owori.domain.schedule.exception;

public class NoAuthorityDeleteException extends IllegalArgumentException {
    private static final String Message = "삭제 권한이 없습니다.";

    public NoAuthorityDeleteException() { super(Message); }
}
