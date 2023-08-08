package com.owori.domain.comment.service;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.dto.response.CommentIdResponse;
import com.owori.domain.comment.dto.response.CommentResponse;
import com.owori.domain.comment.entity.Comment;
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
            // 상위 댓글
            Comment parentComment = Optional.ofNullable(comment.getParentId())
                    .map(this::loadEntity).orElse(null);
            // 삭제
            Story story = comment.getStory();
            story.removeComment(comment);

            // 상위 댓글이 삭제된 경우 체크
            while(parentComment != null) {
                if (parentComment.getContent().equals("삭제된 댓글입니다.")) { // 상위 댓글이 삭제된 경우라면
                    // 해당 상위 댓글에 다른 하위 댓글이 없다면 삭제 처리(반복해서)
                    if (commentRepository.countByParent(parentComment) == 0L) {
                        Comment grandParentComment = parentComment.getParent();
                        story.removeComment(parentComment);
                        parentComment = grandParentComment;
                    }
                }
                else break;
            }
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
