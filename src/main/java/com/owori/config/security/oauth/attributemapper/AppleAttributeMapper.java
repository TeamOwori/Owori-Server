package com.owori.config.security.oauth.attributemapper;

import com.owori.config.security.oauth.OAuth2Request;
import com.owori.domain.member.entity.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppleAttributeMapper implements AttributeMappable {
    @Override
    public OAuth2Request mapToRequest(Map<String, Object> attributes) {
        return new OAuth2Request("", "", AuthProvider.APPLE);
    }
}
