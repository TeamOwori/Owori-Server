package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;

import static com.owori.domain.story.entity.QStory.story;

@RequiredArgsConstructor
public class StoryRepositoryCustomImpl implements StoryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final StoryOrderConverter storyOrderConverter;

    @Override
    public Slice<Story> findAllStory(Pageable pageable, Member member, LocalDate date) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member)
                                .and(storyOrderConverter.createOrderExpression(pageable, date)) // no-offset 페이징 처리
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false; // pagesize보다 1 크게 가져와서 다음 페이지가 남았는지 확인
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}