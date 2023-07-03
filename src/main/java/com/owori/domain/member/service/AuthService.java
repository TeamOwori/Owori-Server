package com.owori.domain.member.service;

import com.owori.config.security.jwt.JwtToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public JwtToken refreshToken(String refreshToken, String accessToken) {
        return null; // todo API Docs를 위한 메소드 생성
    }
}
