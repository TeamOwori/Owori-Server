package com.owori.domain.story.exception;

public class SearchKeywordException extends IllegalArgumentException{
    private static final String MESSAGE = "검색어는 2글자 이상 입력해주세요.";

    public SearchKeywordException() { super(MESSAGE); }
}
