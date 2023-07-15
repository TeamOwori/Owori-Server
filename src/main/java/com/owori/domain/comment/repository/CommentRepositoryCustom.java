package com.owori.domain.comment.repository;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.family.entity.Family;
import com.owori.domain.story.entity.Story;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllComments(Story story, Family family);
}
