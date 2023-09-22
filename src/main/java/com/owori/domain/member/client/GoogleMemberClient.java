package com.owori.domain.member.client;

import com.owori.domain.member.dto.client.GoogleMemberResponse;
import com.owori.domain.member.exception.WebClientException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;


@Component
public class GoogleMemberClient {

    private final WebClient webClient;
    public GoogleMemberClient() { this.webClient = generateWebClient(); }

    public GoogleMemberResponse requestToGoogle(final String token) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_token", token)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(GoogleMemberResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }

    private WebClient generateWebClient() {
        return WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v2/userinfo")
                .build();
    }
}
