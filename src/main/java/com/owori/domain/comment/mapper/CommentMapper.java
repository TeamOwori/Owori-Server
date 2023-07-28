package com.owori.domain.comment.mapper;

import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.comment.entity.Comment;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toEntity(Member member, Story story, Comment parent, String content){
        return Comment.builder()
                .member(member)
                .story(story)
                .parent(parent)
                .content(content)
                .build();
    }

    public CommentResponse toResponse(Comment comment){
        return CommentResponse.builder()
                .parentCommentId(comment.getParentId())
                .commentId(comment.getId())
                .comment(comment.getContent())
                .writer(comment.getMember().getNickname())
                .timeBeforeWriting(comment.getTimeBefore())
                .build();
    }
}
