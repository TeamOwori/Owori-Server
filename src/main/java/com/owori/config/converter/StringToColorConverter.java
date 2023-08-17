package com.owori.config.converter;

import org.springframework.core.convert.converter.Converter;
import com.owori.domain.member.entity.Color;

public class StringToColorConverter implements Converter<String, Color> {
    @Override
    public Color convert(final String source) {
        return Color.valueOf(source.toUpperCase());
    }
}
