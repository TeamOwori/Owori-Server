package com.owori.global.exception;

public class NoAuthorityException extends IllegalArgumentException {
    private static final String MESSAGE = " 권한이 없습니다.";
    public NoAuthorityException(String message) { super(message + MESSAGE); }
}
