package com.owori.domain.member.exception;

public class WebClientException extends IllegalStateException {
    private static final String MESSAGE = "카카오의 요청을 받을 수 없습니다.";

    public WebClientException() {
        super(MESSAGE);
    }
}
