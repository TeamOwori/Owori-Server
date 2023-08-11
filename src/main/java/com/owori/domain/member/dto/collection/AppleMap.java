package com.owori.domain.member.dto.collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AppleMap {
    private final Map<String, String> map;

    public String get(final String key) {
        return map.get(key);
    }
}
