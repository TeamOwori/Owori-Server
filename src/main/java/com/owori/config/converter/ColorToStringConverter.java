package com.owori.config.converter;

import com.owori.domain.member.entity.Color;
import org.springframework.core.convert.converter.Converter;

public class ColorToStringConverter implements Converter<Color, String> {
    @Override
    public String convert(final Color source) {
        return source.name().toLowerCase();
    }
}
