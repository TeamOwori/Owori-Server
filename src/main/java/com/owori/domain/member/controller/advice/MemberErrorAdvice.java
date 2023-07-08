package com.owori.domain.member.controller.advice;

import com.owori.domain.member.exception.JwtProcessingException;
import com.owori.domain.member.exception.NoSuchProfileImageException;
import com.owori.global.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberErrorAdvice {
    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<ErrorResponse> jwtProcessingException(JwtProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchProfileImageException.class)
    public ResponseEntity<ErrorResponse> noSuchProfileImageException(NoSuchProfileImageException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
