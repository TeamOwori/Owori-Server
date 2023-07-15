package com.owori.domain.comment.service;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.mapper.CommentMapper;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.global.dto.IdResponse;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityLoader<Comment, UUID> {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;

    public IdResponse<UUID> addComment(Story story, AddCommentRequest request) {
        Member member = authService.getLoginUser();
        Optional<Comment> parentComment = Optional.ofNullable(request.getParentCommentId())
                .map(parentId -> loadEntity(parentId));

        Comment comment = commentMapper.toEntity(member, story, parentComment.orElse(null), request.getContent());
        commentRepository.save(comment);

        return new IdResponse<>(comment.getId());
    }

    public void removeComment(UUID commentId) {
        Comment comment = loadEntity(commentId);
        Story story = comment.getStory();
        story.removeComment(comment);
    }

    public IdResponse<UUID> updateComment(UUID commentId, UpdateCommentRequest request) {
        Comment comment = loadEntity(commentId);
        comment.updateContent(request.getComment());

        return new IdResponse<>(comment.getId());
    }

    public List<CommentResponse> findComments(Story story, Member member){
        List<Comment> comments = commentRepository.findAllComments(story, member.getFamily());
        return comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
    }

    @Override
    public Comment loadEntity(UUID id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
