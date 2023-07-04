package com.owori.config.security.oauth.attributemapper;

import com.owori.domain.member.entity.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class AttributeMapperFactory {
    private final Map<AuthProvider, AttributeMappable> mapperMap = new EnumMap<>(AuthProvider.class);
    private final KakaoAttributeMapper kakaoAttributeMapper;
    private final AppleAttributeMapper appleAttributeMapper;

    public AttributeMapperFactory(
            KakaoAttributeMapper kakaoAttributeMapper,
            AppleAttributeMapper appleAttributeMapper) {
        this.kakaoAttributeMapper = kakaoAttributeMapper;
        this.appleAttributeMapper = appleAttributeMapper;
        mapperMap.put(AuthProvider.KAKAO, kakaoAttributeMapper);
        mapperMap.put(AuthProvider.APPLE, appleAttributeMapper);
    }

    public AttributeMappable get(AuthProvider authProvider) {
        return mapperMap.get(authProvider);
    }
}
