package com.owori.domain.story.repository;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;

import static com.owori.domain.heart.entity.QHeart.heart;
import static com.owori.domain.story.entity.QStory.story;

@RequiredArgsConstructor
public class StoryRepositoryCustomImpl implements StoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final StoryOrderConverter storyOrderConverter;

    @Override
    public Page<Story> findStoryBySearch(Pageable pageable, String keyword, Family family) {
        List<Story> stories = queryFactory
                .selectFrom(story).distinct()
                .where(
                        story.member.family.eq(family)
                                .and(
                                        story.title.contains(keyword)
                                                .or(story.content.contains(keyword))
                                                .or(story.member.nickname.contains(keyword))
                                )
                )
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(stories, pageable,
                queryFactory.selectFrom(story).distinct()
                        .where(story.member.family.eq(family)
                                        .and(story.title.contains(keyword)
                                                        .or(story.content.contains(keyword))
                                                        .or(story.member.nickname.contains(keyword))))
                        .fetch().size()
        );
    }

    @Override
    public List<Story> findStoryBySearch2(String keyword, Family family) {
        List<Story> results = queryFactory
                .selectFrom(story)
                .where(
                        story.family.eq(family)
                                .and(
                                        story.title.contains(keyword)
                                                .or(story.content.contains(keyword))
                                                .or(story.member.nickname.contains(keyword))
                                )
                )
                .fetch();
        return results;
    }

    @Override
    public Page<Story> findAllStory(Pageable pageable, Family family) {
        List<Story> stories = queryFactory
                .selectFrom(story).distinct()
                .where(story.member.family.eq(family))
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(stories, pageable,
                queryFactory.selectFrom(story).distinct()
                .where(story.member.family.eq(family)).fetch().size()
        );
    }

    @Override
    public Page<Story> findStoryByWriter(Pageable pageable, Member member) {
        List<Story> stories = queryFactory
                .selectFrom(story)
                .where(story.member.eq(member))
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(stories, pageable,
                queryFactory.selectFrom(story).distinct().where(story.member.eq(member)).fetch().size()
        );
    }

    @Override
    public Page<Story> findStoryByHeart(Pageable pageable, Member member) {
        List<Story> stories = queryFactory
                .selectFrom(story).distinct()
                .join(story.hearts, heart)
                .where(heart.member.eq(member))
                .orderBy(storyOrderConverter.convert(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(stories, pageable,
                queryFactory.selectFrom(story).distinct()
                        .join(story.hearts, heart)
                        .where(heart.member.eq(member)).fetch().size()
        );
    }

}