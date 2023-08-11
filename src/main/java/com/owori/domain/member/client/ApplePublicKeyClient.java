package com.owori.domain.member.client;

import com.owori.domain.member.dto.client.ApplePublicKeyResponse;
import com.owori.domain.member.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyClient {
    private final WebClient webClient;

    public ApplePublicKeyClient() {
        this.webClient = generateWebClient();
    }

    public ApplePublicKeyResponse requestToApple() {
        return webClient
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(ApplePublicKeyResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private WebClient generateWebClient() {
        return WebClient.builder()
                .baseUrl("https://appleid.apple.com/auth/keys")
                .build();
    }
}
