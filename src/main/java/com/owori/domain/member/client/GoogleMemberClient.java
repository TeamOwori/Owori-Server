package com.owori.domain.member.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import com.owori.domain.member.dto.client.GoogleMemberResponse;

import com.owori.domain.member.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;


@Component
public class GoogleMemberClient {

    @Value("${social.google.android-client-id}")
    private String androidClientId;

    @Value("${social.google.ios-client-id}")
    private String iosClientId;

    public GoogleMemberResponse requestToGoogle(final String token) {
        // idToken 무결성 확인
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(androidClientId, iosClientId))
                .build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidTokenException();
        }
        Payload payload = idToken.getPayload();

        // 유저 식별자 받기 및 출력 테스트
        String userId = payload.getSubject();

        return new GoogleMemberResponse(userId);
    }


}
