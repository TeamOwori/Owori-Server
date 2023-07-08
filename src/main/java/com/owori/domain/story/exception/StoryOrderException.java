package com.owori.domain.story.exception;

public class StoryOrderException extends IllegalArgumentException{
    private static final String MESSAGE = "sort 값을 확인해주세요.";

    public StoryOrderException() {
        super(MESSAGE);
    }
}
