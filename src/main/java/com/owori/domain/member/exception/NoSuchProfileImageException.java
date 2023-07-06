package com.owori.domain.member.exception;

import java.util.NoSuchElementException;

public class NoSuchProfileImageException extends NoSuchElementException {
    private static final String MESSAGE = "프로필 이미지가 비어있습니다.";

    public NoSuchProfileImageException() {
        super(MESSAGE);
    }
}
