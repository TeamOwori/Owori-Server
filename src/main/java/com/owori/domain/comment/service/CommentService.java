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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityLoader<Comment, UUID> {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final static Long NO_CHILD_COMMENT = 0L;

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
        if(Objects.equals(commentRepository.countByParent(comment), NO_CHILD_COMMENT)) {
            Comment parentComment = comment.getParent();
            Story story = comment.getStory();
            story.removeComment(comment);

            // 상위 댓글 존재 시 하위 댓글이 존재하지 않는 경우 : 삭제
            if(parentComment != null && Objects.equals(commentRepository.countByParent(parentComment), NO_CHILD_COMMENT)) story.removeComment(parentComment);
        }
        // 하위 댓글이 있는 경우 : deleteCheck 필드값 변경(false -> true)
        if(!Objects.equals(commentRepository.countByParent(comment), NO_CHILD_COMMENT)) comment.deleteComment();
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
