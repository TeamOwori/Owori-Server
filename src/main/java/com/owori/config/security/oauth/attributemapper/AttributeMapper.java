package com.owori.config.security.oauth.attributemapper;

import com.owori.config.security.oauth.OAuth2Request;
import com.owori.domain.member.entity.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AttributeMapper {
    private final AttributeMapperFactory attributeMapperFactory;

    public OAuth2Request mapToRequest(AuthProvider authProvider, Map<String, Object> attributes) {
        return attributeMapperFactory.get(authProvider).mapToRequest(attributes);
    }
}
