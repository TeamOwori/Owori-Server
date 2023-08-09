package com.owori.domain.comment.exception;

public class CommentRestrictException extends IllegalStateException {
    private static final String MESSAGE = "대댓글에 대한 대댓글은 달 수 없습니다.";

    public CommentRestrictException() {
        super(MESSAGE);
    }
}
