package com.owori.domain.comment.controller;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.service.CommentService;
import com.owori.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 작성 컨트롤러입니다.
     * @param request 작성할 댓글 정보를 담은 dto 입니다.
     * @return 생성된 댓글의 id를 반환합니다.
     */
    @PostMapping
    public ResponseEntity<IdResponse<UUID>> addComment(@RequestBody AddCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request));
    }

    /**
     * 댓글 삭제 컨트롤러입니다.
     * @param commentId 삭제할 댓글의 id 입니다.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable UUID commentId){
        commentService.removeComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정 컨트롤러입니다.
     * @param commentId 수정할 댓글의 id 입니다.
     * @param request 수정할 내용이 담긴 dto입니다.
     * @return 수정한 댓글의 id를 반환합니다.
     */
    @PostMapping("/{commentId}")
    public ResponseEntity<IdResponse<UUID>> updateComment(@PathVariable UUID commentId, @RequestBody UpdateCommentRequest request){
        return ResponseEntity.ok().body(commentService.updateComment(commentId, request));
    }

}
