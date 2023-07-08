package com.owori.domain.family.exception;

public class InviteCodeDuplicateException extends IllegalStateException {
    private static final String MESSAGE = "초대 코드 중복이 발생했습니다. 다시 시도해주세요.";

    public InviteCodeDuplicateException() {
        super(MESSAGE);
    }
}
