package com.owori.domain.comment.service;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.mapper.CommentMapper;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.service.StoryService;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityLoader<Comment, UUID> {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final StoryService storyService;

    @Transactional
    public IdResponse<UUID> addComment(AddCommentRequest request) {
        Member member = authService.getLoginUser();
        Story story = storyService.loadEntity(request.getStoryId());
        Optional<Comment> parentComment = Optional.ofNullable(request.getParentCommentId())
                .map(parentId -> loadEntity(parentId));

        Comment comment = commentMapper.toEntity(member, story, parentComment.orElse(null), request.getContent());
        commentRepository.save(comment);

        return new IdResponse<>(comment.getId());
    }

    @Transactional
    public void removeComment(UUID commentId) {
        Comment comment = loadEntity(commentId);
        comment.getStory().removeComment(comment);
    }

    public IdResponse<UUID> updateComment(UUID commentId, UpdateCommentRequest request) {
        Comment comment = loadEntity(commentId);
        comment.updateContent(request.getComment());

        return new IdResponse<>(comment.getId());
    }

    @Override
    public Comment loadEntity(UUID id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
