package com.owori.domain.comment.repository;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.family.entity.Family;
import com.owori.domain.story.entity.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.owori.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllComments(Story story, Family family) {
       return queryFactory.selectFrom(comment)
               .where(
                       comment.story.eq(story)
                               .and(comment.member.family.eq(family))
               )
               .orderBy(comment.parent.id.asc().nullsFirst(),
                       comment.baseTime.createdAt.asc())
               .fetch();
    }
}
