package com.owori.domain.member.exception;

public class WebClientException extends IllegalStateException {
    private static final String MESSAGE = "";

    public WebClientException() {
        super(MESSAGE);
    }
}
