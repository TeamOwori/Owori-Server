package com.owori.config.security.oauth.attributemapper;

import com.owori.config.security.oauth.OAuth2Request;
import com.owori.domain.member.entity.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoAttributeMapper implements AttributeMappable {
    @Override
    public OAuth2Request mapToRequest(Map<String, Object> attributes) {
        String accoutId = attributes.get("id").toString();
        String name = ((Map<String, String>)attributes.get("properties")).get("nickname");
        return new OAuth2Request(accoutId, name, AuthProvider.KAKAO);
    }
}
