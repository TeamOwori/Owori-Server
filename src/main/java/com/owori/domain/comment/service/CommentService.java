package com.owori.domain.comment.service;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.dto.response.CommentIdResponse;
import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.exception.CommentRestrictException;
import com.owori.domain.comment.mapper.CommentMapper;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.exception.NoAuthorityException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityLoader<Comment, UUID> {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;

    public CommentIdResponse addComment(Story story, AddCommentRequest request) {
        Member member = authService.getLoginUser();
        Comment parentComment = Optional.ofNullable(request.getParentCommentId())
                .map(this::loadEntity).orElse(null);

        // 대댓글에 대댓글을 달려는지 체크
        if(parentComment != null && parentComment.getParent() != null) throw new CommentRestrictException();

        Comment comment = commentRepository.save(commentMapper.toEntity(member, story, parentComment, request.getContent()));

        return new CommentIdResponse(comment.getId());
    }

    @Transactional
    public void removeComment(UUID commentId) {
        Comment comment = loadEntity(commentId);
        if (!comment.getMember().getId().equals(authService.getLoginUser().getId())) {
            throw new NoAuthorityException();
        }

        // 하위 댓글이 없는 경우 : 삭제
        if(commentRepository.countByParent(comment) == 0L) {
            Story story = comment.getStory();
            story.removeComment(comment);
        }
        // 하위 댓글이 있는 경우 : 내용 변경
        else comment.deleteComment();
    }

    @Transactional
    public CommentIdResponse updateComment(UpdateCommentRequest request) {
        Comment comment = loadEntity(request.getCommentId());
        if (!comment.getMember().getId().equals(authService.getLoginUser().getId())) {
            throw new NoAuthorityException();
        }
        comment.updateContent(request.getComment());

        return new CommentIdResponse(comment.getId());
    }

    public List<CommentResponse> findComments(Story story, Member member) {
        List<Comment> comments = commentRepository.findAllComments(story, member.getFamily());
        return comments.stream().map(commentMapper::toResponse).toList();
    }

    @Override
    public Comment loadEntity(UUID id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
