package com.owori.domain.story.exception;

public class InvalidUserException extends IllegalArgumentException {
    private static final String MESSAGE = "권한이 없는 유저 입니다.";

    public InvalidUserException() {
        super(MESSAGE);
    }
}