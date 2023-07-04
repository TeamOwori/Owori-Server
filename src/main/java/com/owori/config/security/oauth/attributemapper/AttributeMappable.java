package com.owori.config.security.oauth.attributemapper;

import com.owori.config.security.oauth.OAuth2Request;

import java.util.Map;

public interface AttributeMappable {
    OAuth2Request mapToRequest(Map<String, Object> attributes);
}
