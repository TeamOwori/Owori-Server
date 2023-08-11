package com.owori.global.exception;

public class InvalidDateException extends IllegalArgumentException{
    private static final String MESSAGE = "종료일이 시작일보다 앞설 수 없습니다";

    public InvalidDateException() {
        super(MESSAGE);
    }
}
