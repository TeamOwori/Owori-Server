package com.owori.domain.family.exception;

public class InviteCodeExistException extends IllegalStateException {
    private static final String MESSAGE = "이미 초대코드가 유효합니다.";

    public InviteCodeExistException() {
        super(MESSAGE);
    }
}
