package com.owori.domain.member.controller;

import com.owori.config.security.jwt.JwtToken;
import com.owori.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * Jwt 검증 및 재발급 컨트롤러입니다.
     * 헤더로 리프레시 토큰과 액세스 토큰을 받아와 검증하고 액세스 토큰의 검증에 실패해도 리프레시 토큰이 성공하면 재발급합니다.
     * @param refreshToken 리프레시 토큰입니다.
     * @param accessToken 액세스 토큰입니다. 리프레시 토큰보다 짧은 유효기간을 갖습니다.
     * @return JwtToken은 액세스 토큰과 리프레시 토큰입니다. 상황에 따라 새롭게 발급된 토큰일 수 있습니다.
     */
    @GetMapping("/refresh")
    public ResponseEntity<JwtToken> authorize(
            @RequestHeader("refresh_token") String refreshToken,
            @RequestHeader("access_token") String accessToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken, accessToken));
    }
}
