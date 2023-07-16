package com.owori.domain.story.repository;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;

import static com.owori.domain.heart.entity.QHeart.heart;
import static com.owori.domain.story.entity.QStory.story;

@RequiredArgsConstructor
public class StoryRepositoryCustomImpl implements StoryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final StoryOrderConverter storyOrderConverter;

    @Override
    public Slice<Story> findAllStory(Pageable pageable, Family family, LocalDate date) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.family.eq(family)
                                .and(storyOrderConverter.createOrderExpression(pageable, date)) // no-offset 페이징 처리
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    @Override
    public Slice<Story> findStoryBySearch(Pageable pageable, String keyword, Family family, LocalDate date) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.family.eq(family)
                                .and(storyOrderConverter.createOrderExpression(pageable, date))
                                .and(
                                        story.title.contains(keyword)
                                                .or(story.contents.contains(keyword))
                                                .or(story.member.nickname.contains(keyword))
                                )
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    @Override
    public Slice<Story> findStoryByWriter(Pageable pageable, Member member, LocalDate date) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.member.eq(member)
                                .and(storyOrderConverter.createOrderExpression(pageable, date))
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    @Override
    public Slice<Story> findStoryByHeart(Pageable pageable, Member member, LocalDate date) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .join(story.hearts, heart)
                .where(
                        heart.member.eq(member)
                                .and(storyOrderConverter.createOrderExpression(pageable, date))
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    private Slice<Story> checkLastPage(Pageable pageable, List<Story> results) {
        boolean hasNext = false; // pagesize보다 1 크게 가져와서 다음 페이지가 남았는지 확인

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}