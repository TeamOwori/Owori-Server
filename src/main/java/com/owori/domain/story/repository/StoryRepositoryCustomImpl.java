package com.owori.domain.story.repository;

import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    /*
     *  최신순 조회 - createAt 기준
     */
    @Override
    public Slice<Story> findAllStoryByCreatedAt(Pageable pageable, Member member, LocalDate createdAt) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member)
                                .and(ltStoryCreateAt(createdAt)
                        ))
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    /*
     *  날짜순 조회 - StartDate 기준
     */
    @Override
    public Slice<Story> findAllStoryByEventAt(Pageable pageable, Member member, LocalDate startDate) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member)
                                .and(ltStoryStartDate(startDate)
                                ))
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    private BooleanExpression ltStoryCreateAt(LocalDate createAt) { return createAt == null ? null : story.baseTime.createdAt.lt(createAt.atStartOfDay()); }

    private BooleanExpression ltStoryStartDate(LocalDate startDate) { return startDate == null ? null : story.startDate.lt(startDate); }

    private Slice<Story> checkLastPage(Pageable pageable, List<Story> results) {
        boolean hasNext = false; // pagesize보다 1 크게 가져와서 다음 페이지가 남았는지 확인
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

}