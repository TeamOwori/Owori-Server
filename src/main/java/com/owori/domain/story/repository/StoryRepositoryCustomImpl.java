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

    /*
    *  최신순 앨범형 조회
    */
    @Override
    public Slice<Story> findAllStoryByCreateAt(Pageable pageable, Long lastId, Member member) {
        List<Story> storyList = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member),
                        ltStoryId(lastId)
                )
                .orderBy(story.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, storyList);
    }

    private BooleanExpression ltStoryId(Long storyId) {
        return storyId == null ? null : story.id.lt(storyId);
    }

    /*
     *  날짜순 앨범형 조회
     */
    @Override
    public Slice<Story> findAllStoryByEventAt(Pageable pageable, LocalDate startDate, Member member) {
        List<Story> storyList = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member),
                        ltStoryStartDate(startDate)
                )
                .orderBy(story.startDate.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, storyList);
    }

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