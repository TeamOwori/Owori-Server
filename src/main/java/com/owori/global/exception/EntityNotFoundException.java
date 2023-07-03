package com.owori.global.exception;

public class EntityNotFoundException extends IllegalArgumentException {
    private static final String MESSAGE = "엔티티를 찾을 수 없습니다";

    public EntityNotFoundException() {
        super(MESSAGE);
    }
}
