package com.owori.config.security.jwt;

import com.owori.config.security.oauth.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class JwtSetupService {
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.access-header}")
    private String accessTokenHeaderTag;

    @Value("${jwt.refresh-header}")
    private String refreshTokenHeaderTag;

    public void addJwtTokensInCookie(HttpServletResponse response, UserPrinciple loginUser) {
        JwtToken jwtToken = jwtTokenProvider.createToken(loginUser);
        ResponseCookie accessTokenCookie =
                setCookie(accessTokenHeaderTag, jwtToken.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        ResponseCookie refreshTokenCookie =
                setCookie(refreshTokenHeaderTag, jwtToken.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie setCookie(String key, String value) {
        return ResponseCookie.from(key, value).path("/").httpOnly(true).build();
    }
}
