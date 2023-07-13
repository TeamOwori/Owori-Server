package com.owori.domain.member.client;

import com.owori.domain.member.dto.client.KakaoMemberResponse;
import com.owori.domain.member.exception.WebClientException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class KakaoMemberClient {
    private final WebClient webClient;

    public KakaoMemberClient() {
        this.webClient = generateWebClient();
    }

    public KakaoMemberResponse requestToKakao(final String token) {
        return webClient
                .get()
                .headers(headers -> headers.setBearerAuth(token))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(KakaoMemberResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private WebClient generateWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build();
    }
}
