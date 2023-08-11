package com.owori.domain.member.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owori.domain.member.dto.collection.AppleMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.security.PublicKey;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtParser {
    private static final String TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;
    private final ObjectMapper objectMapper;

    public AppleMap parseHeader(final String idToken) throws JsonProcessingException {
        String encodedHeader = idToken.split(TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
        String decodedHeader = new String(Base64Utils.decodeFromUrlSafeString(encodedHeader));
        return new AppleMap(objectMapper.readValue(decodedHeader, Map.class));
    }

    public String parseClaims(final String idToken, final PublicKey publicKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(idToken)
                .getBody();
        return claims.get("sub", String.class);
    }
}
