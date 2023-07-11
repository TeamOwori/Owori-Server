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
        Comment parent = request.getParentCommentId() == null ? null : loadEntity(request.getParentCommentId());
        Story story = storyService.loadEntity(request.getStoryId());

        Comment comment = commentMapper.toEntity(member, story, parent, request.getContent());
        commentRepository.save(comment);
        story.addComment(comment);
        comment.updateParent(parent);

        return new IdResponse<>(comment.getId());
    }

    @Transactional
    public void removeComment(Long storyId, UUID commentId) {
        Story story = storyService.loadEntity(storyId);
        Comment comment = loadEntity(commentId);
        story.removeComment(comment);
        comment.delete();
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
