package com.owori.domain.family.exception;

public class InviteCodeDuplicateException extends IllegalStateException {
    private static final String MESSAGE = "초대 코드가 중복됩니다.";

    public InviteCodeDuplicateException() {
        super(MESSAGE);
    }
}
