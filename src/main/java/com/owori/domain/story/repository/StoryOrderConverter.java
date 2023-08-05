package com.owori.domain.story.repository;

import com.owori.global.converter.OrderConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        keywordMap.put("created_at", story.baseTime.createdAt);
        keywordMap.put("start_date", story.startDate);
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

    public BooleanExpression createOrderExpression(Pageable pageable, LocalDate date) {
        if (date == null) {
            return null;
        }
        String sortProperty = pageable.getSort().toList().get(0).getProperty();

        if (sortProperty.equals("start_date")) {
            return story.startDate.lt(date);
        }
        return story.baseTime.createdAt.lt(date.atStartOfDay());
    }
}
