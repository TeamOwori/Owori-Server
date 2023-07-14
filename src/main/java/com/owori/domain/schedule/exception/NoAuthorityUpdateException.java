package com.owori.domain.schedule.exception;

public class NoAuthorityUpdateException extends IllegalArgumentException{
    private static final String Message = "수정 권한이 없습니다.";

    public NoAuthorityUpdateException(){
        super(Message);
    }
}
