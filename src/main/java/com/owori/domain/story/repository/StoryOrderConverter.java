package com.owori.domain.story.repository;

import com.owori.global.converter.OrderConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.owori.domain.story.entity.QStory.story;


@Component
public class StoryOrderConverter implements OrderConverter {
    private final Map<String, ComparableExpressionBase<?>> keywordMap = new HashMap<>();

    public StoryOrderConverter() {
        initializeMap();
    }

    private void initializeMap() {
        keywordMap.put("createAt", story.baseTime.createdAt);
        keywordMap.put("startDate", story.startDate);
    }

    @Override
    public OrderSpecifier<?>[] convert(Sort sort) {
        return sort.stream()
                .map(
                        s -> {
                            ComparableExpressionBase<?> path = keywordMap.get(s.getProperty());
                            return path.desc();
                        })
                .toArray(OrderSpecifier[]::new);
    }
}

