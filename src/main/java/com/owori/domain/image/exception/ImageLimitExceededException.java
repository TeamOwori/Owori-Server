package com.owori.domain.image.exception;

public class ImageLimitExceededException extends IllegalStateException {
    private static final String MESSAGE = "업로드 가능한 이미지 개수(10)를 초과했습니다";

    public ImageLimitExceededException() {
        super(MESSAGE);
    }
}

