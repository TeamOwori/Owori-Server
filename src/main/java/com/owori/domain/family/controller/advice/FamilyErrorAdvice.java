package com.owori.domain.family.controller.advice;

import com.owori.domain.family.exception.InviteCodeDuplicateException;
import com.owori.global.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FamilyErrorAdvice {
    @ExceptionHandler(InviteCodeDuplicateException.class)
    public ResponseEntity<ErrorResponse> inviteCodeDuplicateException(InviteCodeDuplicateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
